package cn.edu.sdwu.android02.classroom.sn170507180209_02;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Ch16Activity1 extends AppCompatActivity {

    private TextureView                textureView;
    private SurfaceTexture             surfaceTexture;
    private CameraDevice.StateCallback stateCallback;
    private CameraDevice               cameraDevice;
    private CaptureRequest.Builder     captureRequestBuilder;//请求的构造器
    private CaptureRequest             previewRequest;
    private CameraCaptureSession       cameraCaptureSession;
    private ImageReader                imageReader;//接收相机生成的静态组件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检查相机的使用权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.CAMERA);
            if (result == PackageManager.PERMISSION_GRANTED) {
                setCameraLayout();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
            }
        }

        //实例化StateCallback，它用来当打开相机时，执行的方法（便于我们进行会话的创建）
        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                //摄像头打开后，执行本方法，可以获取CameraDevice对象
                Ch16Activity1.this.cameraDevice = cameraDevice;
                //准备预览时使用的组件
                Surface surface = new Surface(surfaceTexture);
                try {
                    //创建一个捕捉请求CaptureRequest
                    captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    captureRequestBuilder.addTarget(surface);//指定视频输出的位置

                    imageReader = ImageReader.newInstance(1024, 168, ImageFormat.JPEG, 2);
                    imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                        @Override
                        public void onImageAvailable(ImageReader imageReader) {
                            //当照片数据可用时，激发该方法
                            //获取捕获的照片数据
                            Image image = imageReader.acquireNextImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            //写文件
                            File file = new File(Environment.getExternalStorageDirectory(), "adcb.jpg");
                            FileOutputStream outputStream = null;
                            try {
                                outputStream = new FileOutputStream(file);
                                outputStream.write(bytes);
                                Toast.makeText(Ch16Activity1.this, "save:" + file, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.i(Ch16Activity1.class.toString(), e.toString());
                            } finally {
                                try {
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (Exception e) {
                                    Log.i(Ch16Activity1.class.toString(), e.toString());
                                }
                            }
                        }
                    }, null);

                    //创建一个相机捕捉会话
                    // 参数1代表后续预览或拍照使用的组件
                    //参数2代表的是监听器，创建会话完成后执行的方法
                    cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            //会话创建完成后，我们可以在参数中得到会话的对象
                            //开始显示相机的预览
                            Ch16Activity1.this.cameraCaptureSession = cameraCaptureSession;
                            try {
                                previewRequest = captureRequestBuilder.build();
                                //在会话中发出重复请求，进行预览(捕获连续的图像)
                                cameraCaptureSession.setRepeatingRequest(previewRequest, null, null);
                            } catch (Exception e) {
                                Log.e(Ch16Activity1.class.toString(), e.toString());
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                        }
                    }, null);
                } catch (Exception e) {

                }

            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                //摄像头关闭时，执行本方法。
                Ch16Activity1.this.cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {

            }
        };
    }

    private void setCameraLayout() {
        //用户授权后，加载界面
        setContentView(R.layout.layout_ch16_1);
        textureView = (TextureView) findViewById(R.id.ch16_tv);
        //当textureView准备好之后，自动调用setSurfaceTextureListener的监听器
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                //当textureView可用时，打开摄像头
                Ch16Activity1.this.surfaceTexture = surfaceTexture;
                openCamera(width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    private void openCamera(int width, int height) {
        //使用CameraManager得到CameraManager相机管理器
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraManager.openCamera("0", stateCallback, null);//“0”代表后置摄像头，“1”代表前置摄像头
        } catch (Exception e) {
            Log.i(Ch16Activity1.class.toString(), e.toString());
        }

    }

    public void call(View view) {
        //判断当前版本是否是6.0以后的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1.判断当前用户是否已经授权过；
            int result = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 101);
            }
        }
    }

    public void sms(View view) {
        //判断当前版本是否是6.0以后的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1.判断当前用户是否已经授权过；
            int result = checkSelfPermission(Manifest.permission.SEND_SMS);
            if (result == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 102);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }
        }
        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            }
        }
        if (requestCode == 104) {//相机的授权
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCameraLayout();
            }
        }
    }

    public void chnOri() {
        //改变屏幕方向
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void takePhoto(View view) {
        //点击快门，生成静态图像
        if (cameraDevice != null) {
            //使用Builder创建请求
            try {
                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                builder.addTarget(imageReader.getSurface());
                //停止连续取景
                cameraCaptureSession.stopRepeating();
                //捕获静态图像
                cameraCaptureSession.capture(builder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        //捕获完成后，恢复连续取景
                        try {
                            session.setRepeatingRequest(previewRequest, null, null);
                        } catch (CameraAccessException e) {
                            Log.i(Ch16Activity1.class.toString(), e.toString());
                        }
                    }
                }, null);
            } catch (CameraAccessException e) {
                Log.i(Ch16Activity1.class.toString(), e.toString());
            }

        }

    }

    private void sendSms() {
        //借助于SmsManager工具进行发送
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("15610158635", "15665750807", "hello world", null, null);
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://15610158635"));
        startActivity(intent);
    }


}
