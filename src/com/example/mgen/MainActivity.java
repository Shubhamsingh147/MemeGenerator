package com.example.mgen;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.View;
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



public class MainActivity extends Activity implements android.view.View.OnClickListener{
	
	 private Uri mImageCaptureUri;
	 private ImageView mImageView,imageview1;
	 private RelativeLayout centre;
	 private HorizontalScrollView horizLay;
	 private LinearLayout inHorizontalScrollView;
	 private static final int PICK_FROM_CAMERA = 1;
	 private static final int PICK_FROM_FILE = 2;
	 private boolean doubleBackToExitPressedOnce = false;
	 private Button reset,save;
	 TextView tv2,tvtop,tvbottom;
	 EditText top,bottom;
	 Bitmap bitmap;
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
	     top.setVisibility(View.GONE);
	     bottom.setVisibility(View.GONE);
		 reset = (Button) findViewById(R.id.reset);
         save = (Button) findViewById(R.id.save);
         tv2 = (TextView) findViewById(R.id.textView2);
		 reset.setOnClickListener(this);
		 save.setOnClickListener(this); 
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
		 
	     horizLay = (HorizontalScrollView) findViewById(R.id.hl);
		 centre =(RelativeLayout) findViewById(R.id.centreImage);
	     mImageView = (ImageView) findViewById(R.id.iv_pic);
	     mImageView.setOnClickListener(this);
	     imageview1 = (ImageView) findViewById(R.id.imageView1);
        final String [] items           = new String [] {"From Camera", "From SD Card"};
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
     //bring Image From SD_Card
	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode != RESULT_OK) return;
	 
	        bitmap   = null;
	        String path     = "";
	 
	        if (requestCode == PICK_FROM_FILE) {
	            mImageCaptureUri = data.getData();
	            path = getRealPathFromURI(mImageCaptureUri); //from Gallery
	 
	            if (path == null)
	                path = mImageCaptureUri.getPath(); //from File Manager
	 
	            if (path != null)
	                bitmap  = BitmapFactory.decodeFile(path);
	        } else {
	            path    = mImageCaptureUri.getPath();
	            bitmap  = BitmapFactory.decodeFile(path);
	        }
	        mImageView.setVisibility(View.VISIBLE);
	        mImageView.setImageBitmap(bitmap);
	    }
	 //for Camera Image Purposes TODO:errors
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
			// TODO Auto-generated method stub
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
					Toast.makeText(this, "First fill the text bottom and Up", Toast.LENGTH_LONG).show();
				}
				else 
				{	
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
					    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG).show();
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
			}
			else
			{
				horizLay.setVisibility(View.GONE);
				top.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
				imageview1.setBackgroundResource(R.drawable.samp);
				centre.setVisibility(View.VISIBLE);
				tv2.setVisibility(View.GONE);
			}
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
		    paint.setTextSize(convertToPixels(MainActivity.this, 11));
		
		    Rect textRect = new Rect();
		    paint.getTextBounds(text, 0, text.length(), textRect);
		
		    Canvas canvas = new Canvas(bm);
		
		    //If the text is bigger than the canvas , reduce the font size
		    if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
		        paint.setTextSize(convertToPixels(MainActivity.this, 7));        //Scaling needs to be used for different dpi's
		
		    //Calculate the positions
		    int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset
		
		    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
		    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  
		
		    canvas.drawText(text, xPos, yPos, paint);
		
		    return new BitmapDrawable(getResources(), bm);
		}
	 public static int convertToPixels(Context context, int nDP)
		{
		    final float conversionScale = context.getResources().getDisplayMetrics().density;
		
		    return (int) ((nDP * conversionScale) + 0.5f) ;
		
		}
		@Override
	 public void onBackPressed() {
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show();
	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	} 
	
}