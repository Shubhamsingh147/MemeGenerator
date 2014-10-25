package com.example.mgen;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//TODO: write to Internal MEmory and check if SD_Card present?-urgent
//TODO: write text to correct place-easy
//TODO: insert many preImages in array-easy
//TODO: Correct image not being written-urgent semi
public class MainActivity extends Activity implements android.view.View.OnClickListener{
	
	 private Uri mImageCaptureUri;
	 private ImageView mImageView,imageview1;
	 private RelativeLayout centre;
	 private HorizontalScrollView horizLay;
	 private LinearLayout inHorizontalScrollView;
	 private boolean doubleBackToExitPressedOnce = false;
	 private Button reset,save;
	 ImageButton paint;
	 TextView tv2,tv3,tvtop,tvbottom;
	 EditText top,bottom;
	 Bitmap bitmap;
	 ProgressDialog progressDialog;
	 private static final int PICK_FROM_CAMERA = 1;
	 private static final int PICK_FROM_FILE = 2;
	 private static final int IMAGES_NUMBER=3;
	 public void onCreate(Bundle AddingNames) {
		super.onCreate(AddingNames);
		setContentView(R.layout.activity_main);
		
		inHorizontalScrollView = (LinearLayout)findViewById(R.id.images);
		ImageView[] preImages = new ImageView[IMAGES_NUMBER];
		for(int j=0;j<IMAGES_NUMBER;j++)
		{
			preImages[j]= new ImageView(this);
			LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(300,300);
			preImages[j].setLayoutParams(vp);
			preImages[j].setOnClickListener(this);
			vp.setMargins(5, 5, 5, 5);
			preImages[j].setScaleType(ImageView.ScaleType.FIT_XY);
			preImages[j].setBackgroundResource(R.drawable.shapes);
			preImages[j].setImageResource(R.drawable.samp);
			preImages[j].setTag(R.drawable.samp);
			inHorizontalScrollView.addView(preImages[j]);
		}
		 top = (EditText) findViewById(R.id.ettop);
		 bottom = (EditText) findViewById(R.id.etbottom);
		 tvtop = (TextView) findViewById(R.id.tvtop);
         tvbottom = (TextView) findViewById(R.id.tvbottom);
         reset = (Button) findViewById(R.id.reset);
		 paint = (ImageButton) findViewById(R.id.paint);
         save = (Button) findViewById(R.id.save);
         tv2 = (TextView) findViewById(R.id.textView2);
         tv3 = (TextView) findViewById(R.id.textView3);
         horizLay = (HorizontalScrollView) findViewById(R.id.hl);
		 centre =(RelativeLayout) findViewById(R.id.centreImage);
	     mImageView = (ImageView) findViewById(R.id.iv_pic);
	     imageview1 = (ImageView) findViewById(R.id.imageView1);
	     progressDialog=new ProgressDialog(this);   
         top.setVisibility(View.GONE);
	     bottom.setVisibility(View.GONE);
		 reset.setOnClickListener(this);
		 paint.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CaptureSignature.class); 
                startActivityForResult(intent,3);	
			}
		});
		 save.setOnClickListener(this); 
		 mImageView.setOnClickListener(this);
	     
		 top.addTextChangedListener(new TextWatcher(){
			  @Override
			  public void afterTextChanged(Editable arg0) {}
			  @Override
			  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			      int arg3) {}
			  @Override
			  public void onTextChanged(CharSequence s, int a, int b, int c) {
			    tvtop.setText(s);
			  }});
		 bottom.addTextChangedListener(new TextWatcher(){
			  @Override
			  public void afterTextChanged(Editable arg0) {}
			  @Override
			  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			      int arg3) {}
			  @Override
			  public void onTextChanged(CharSequence s, int a, int b, int c) {
			    tvbottom.setText(s);
			  }});
		 
		 final String [] items           = new String [] {"From Camera","From Gallery"};
        ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder     = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) {
                if (item == 0) {
                    Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file        = new File(Environment.getExternalStorageDirectory(),
                                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    mImageCaptureUri = Uri.fromFile(file);
 
                    try {
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
 
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
 
                    dialog.cancel();
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );
        final AlertDialog dialog = builder.create();        
        ((ImageButton) findViewById(R.id.btn_choose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
	 }
	 
	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 switch(requestCode) { 
		    case PICK_FROM_FILE:
		        if(resultCode == RESULT_OK){  
					mImageCaptureUri=data.getData();
					bitmap   = null;
					String path     = "";
					path = getRealPathFromURI(mImageCaptureUri); //from Gallery
					if (path == null)
					    path = mImageCaptureUri.getPath(); //from File Manager
					if (path != null)
					    bitmap  = BitmapFactory.decodeFile(path);
					else {
					   path    = mImageCaptureUri.getPath(); //from URI
					   try {
						bitmap  = decodeUri(mImageCaptureUri);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 }
					mImageView.setVisibility(View.VISIBLE);
					mImageView.setImageBitmap(bitmap); //from bitmap
					
		        }
		        break;
		    case IMAGES_NUMBER:
		    	if (resultCode == RESULT_OK) {
					 Bundle bundle = data.getExtras();
					 String status  = bundle.getString("status");
					 if(status.equalsIgnoreCase("done")){
						 Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
					     toast.setGravity(Gravity.CENTER, 105, 50);
					     toast.show();
					 }
		    	}
		    	break;
		 	}
	 }
	 
	 private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

	        // The new size we want to scale to
	        final int REQUIRED_SIZE = 140;

	        // Find the correct scale value. It should be the power of 2.
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if (width_tmp / 2 < REQUIRED_SIZE
	               || height_tmp / 2 < REQUIRED_SIZE) {
	                break;
	            }
	            width_tmp /= 2;
	            height_tmp /= 2;
	            scale *= 2;
	        }

	        // Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

	    }
	 
     public String getRealPathFromURI(Uri contentUri) {
	        String [] proj      = {MediaStore.Images.Media.DATA};
	        Cursor cursor       = managedQuery( contentUri, proj, null, null,null);
	 
	        if (cursor == null) return null;
	 
	        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 
	        cursor.moveToFirst();
	 
	        return cursor.getString(column_index);
	    }
	
     @Override
	 public void onClick(View arg0) {
			int id = arg0.getId();
			if(id==R.id.reset)
			{
				Intent newIntent=new Intent(this,MainActivity.class);
				startActivity(newIntent);
				finish();
			}
			else if(id==R.id.save)
			{
				
				if(top.getText().length()==0 || bottom.getText().length()==0)
				{
					Log.d("Value of ET's",top.getText()+" "+bottom.getText());
					Toast toaster=Toast.makeText(this, "First fill the text bottom and Up", Toast.LENGTH_LONG);
					toaster.setGravity(Gravity.CENTER_VERTICAL, 105, 50);
					toaster.show();
				}
				else 
				{	open();
					BitmapDrawable bmp=writeTextOnDrawable(R.drawable.samp, top.getText()+"",bottom.getText()+"");
					Bitmap bm=((BitmapDrawable)bmp).getBitmap();
					FileOutputStream out = null;
					File newFolder = new File(Environment.getExternalStorageDirectory(), "Memes");
				    if (!newFolder.exists()) {
				        newFolder.mkdir();
				    }
					try {
						File file = new File(newFolder, "mgen"+Long.toString(Double.doubleToLongBits(Math.random()))+".jpg");
					    out = new FileOutputStream(file);
					    bm.compress(Bitmap.CompressFormat.PNG, 90, out);
					    save.setVisibility(View.INVISIBLE);
					    Toast toaster=Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG);
						toaster.setGravity(Gravity.CENTER_VERTICAL, 105, 50);
						toaster.show();
					    if(progressDialog!=null)
					    	progressDialog.dismiss();
					    Intent newIntent=new Intent(this,MainActivity.class);
						startActivity(newIntent);
						finish();
					    
					} catch (Exception e) {
					    e.printStackTrace();
					} finally {
					    try {
					        if (out != null) {
					            out.close();
					        }
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					}
				}
			}
			else if(id==R.id.iv_pic)
			{
				horizLay.setVisibility(View.GONE);
				top.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
				imageview1.setImageBitmap(bitmap);
				centre.setVisibility(View.VISIBLE);
				tv2.setVisibility(View.GONE);
				tv3.setVisibility(View.GONE);
				reset.setVisibility(View.VISIBLE);
				save.setVisibility(View.VISIBLE);
			}
			else
			{
				horizLay.setVisibility(View.GONE);
				top.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
				imageview1.setBackgroundResource(R.drawable.samp);
				centre.setVisibility(View.VISIBLE);
				tv2.setVisibility(View.GONE);
				tv3.setVisibility(View.GONE);
				reset.setVisibility(View.VISIBLE);
				save.setVisibility(View.VISIBLE);
			}
		}
	 
     public boolean isExternalStorageWritable() {
    	    String state = Environment.getExternalStorageState();
    	    if (Environment.MEDIA_MOUNTED.equals(state)) {
    	        return true;
    	    }
    	    return false;
    	}
	
     public boolean isExternalStorageReadable() {
    	    String state = Environment.getExternalStorageState();
    	    if (Environment.MEDIA_MOUNTED.equals(state) ||
    	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	        return true;
    	    }
    	    return false;
    	}
     
     private BitmapDrawable writeTextOnDrawable(int drawableId, String text, String text2) {
		
		    Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
		            .copy(Bitmap.Config.ARGB_8888, true);
		
		    Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
		
		    Paint paint = new Paint();
		    paint.setStyle(Paint.Style.FILL);
		    paint.setColor(Color.WHITE);
		    paint.setTypeface(tf);
		    paint.setTextAlign(Align.CENTER);
		    paint.setTextSize(convertToPixels(MainActivity.this, 120));
		
		    Rect textRect = new Rect();
		    paint.getTextBounds(text, 0, text.length(), textRect);
		    paint.getTextBounds(text2, 0, text2.length(), textRect);
		    Canvas canvas = new Canvas(bm);
/*		
		    if(textRect.width() >= (canvas.getWidth() - 4))
		        paint.setTextSize(convertToPixels(MainActivity.this, 7));
		
		    int xPos = (canvas.getWidth() / 2) - 2;
		    int xPos2 = (canvas.getWidth() / 2) - 2;

		    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  
		    int yPos2 = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
		    */
		    int xPos = canvas.getWidth()/9;
		    int xPos2 = canvas.getWidth()/9;
		    int yPos = (int) ((canvas.getHeight()/9));
		    int yPos2 = (int) ((canvas.getHeight()-canvas.getHeight()/9));
		    canvas.drawText(text, xPos, yPos, paint);
		    canvas.drawText(text2, xPos2, yPos2, paint);
		    return new BitmapDrawable(getResources(), bm);
		}
	 
     public static int convertToPixels(Context context, int nDP) {
		    final float conversionScale = context.getResources().getDisplayMetrics().density;
		
		    return (int) ((nDP * conversionScale) + 0.5f) ;
		
		}
	 
     public void open(){
         progressDialog.setMessage("Generating your Meme and saving :) ");
         progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         progressDialog.setIndeterminate(true);
         Log.d("shown","here");
         progressDialog.show();

      final int totalProgressTime = 100;

      final Thread t = new Thread(){

      @Override
      public void run(){
    
         int jumpTime = 0;
         while(jumpTime < totalProgressTime){
            try {
               sleep(200);
               jumpTime += 5;
               progressDialog.setProgress(jumpTime);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

         }

      }
      };
      t.start();

      }
     
     @Override
	 public void onBackPressed() {
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast toast=Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 105, 50);
        toast.show(); 
	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	} 
	
}