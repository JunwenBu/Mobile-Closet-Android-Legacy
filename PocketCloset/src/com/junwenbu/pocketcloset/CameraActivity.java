/* CIS 600 Final Project - Pocket Closet
 * Version: 1.1
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.junwenbu.data.Item;
import com.junwenbu.data.ItemService;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.Button;
import android.widget.Toast;

// control camera
public class CameraActivity extends Activity {
	private Camera camera;
	private String filename;
	private File directory;
	private final String ALBUM_NAME = "ClosetAlbum";
	private Button shootBtn, focusBtn;
	private boolean isCameraWorking;
	private boolean takePhotoSucceful;
	ItemService itemservice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set application to full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_camera);

		shootBtn = (Button) findViewById(R.id.cameraBtnShoot);
		focusBtn = (Button) findViewById(R.id.cameraBtnFocus);
		isCameraWorking = false;
		takePhotoSucceful = false;
		itemservice = new ItemService(this);
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceView.getHolder().setFixedSize(240, 320);
		surfaceView.getHolder().setKeepScreenOn(true);
		surfaceView.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					camera = Camera.open(); // open camera
					Camera.Parameters parameters = camera.getParameters();
					parameters.setPreviewSize(240, 320);
					parameters.setPictureSize(240, 320);
					parameters.setJpegQuality(60);
					// get parameters of camera
					// Log.i("Camera", parameters.flatten());
					camera.setParameters(parameters);
					camera.setPreviewDisplay(holder);
					camera.startPreview();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				/*
				 * must release the camera when destroyed, other app may use the
				 * camera.
				 */
				if (camera != null) {
					camera.release();
					camera = null;
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				//
			}
		}); // end of addCallback
	} // end of onCreate

	// get album directory
	public File getAlbumStorageDir(Context context, String albumName) {
		// Get the directory for the app's private pictures directory.

		if (directory == null) {
			directory = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					albumName);
			if (!directory.mkdirs()) {
				// Log.i("Directory", "Cannot create directory.");
			}
		}
		return directory;
	}

	// called when the user click the "Take Picture" button
	public void clickBtnShoot(View view) {
		shootBtn.setVisibility(ViewGroup.INVISIBLE);
		focusBtn.setVisibility(ViewGroup.INVISIBLE);
		if (!isCameraWorking) {
			isCameraWorking = true;
			takePhotoSucceful = false;
			Thread cameraThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						filename = System.currentTimeMillis() + ".jpg";
						camera.takePicture(null, null, new PictureCallback() {
							@Override
							public void onPictureTaken(byte[] data,
									Camera camera) {
								try {
									File jpgFile = new File(
											getAlbumStorageDir(
													getApplicationContext(),
													ALBUM_NAME), filename);

									FileOutputStream outputStream = new FileOutputStream(
											jpgFile);
									outputStream.write(data);
									outputStream.close();
									// here we need to start preview
									camera.startPreview();
									takePhotoSucceful = true;
								} catch (Exception e) {
									// e.printStackTrace();
									takePhotoSucceful = false;
								}

								if (takePhotoSucceful) {
									Item item = new Item(1, filename, 0, -1,
											-1, 0, 0);
									itemservice.insert(item);
								}
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										shootBtn.setVisibility(ViewGroup.VISIBLE);
										focusBtn.setVisibility(ViewGroup.VISIBLE);
										isCameraWorking = false;
									}
								});

							} // end of onPictureTaken
						}); // end takePicture

					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								shootBtn.setVisibility(ViewGroup.VISIBLE);
								focusBtn.setVisibility(ViewGroup.VISIBLE);
								isCameraWorking = false;
							}
						});
					}
				}
			});

			try {
				cameraThread.start();
			} catch (Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						shootBtn.setVisibility(ViewGroup.VISIBLE);
						focusBtn.setVisibility(ViewGroup.VISIBLE);
						isCameraWorking = false;
					}
				});
			}
		} // end if
	}

	// called when the user click the "Focus" button
	public void clickBtnFocus(View view) {
		camera.autoFocus(null);
	}
}
