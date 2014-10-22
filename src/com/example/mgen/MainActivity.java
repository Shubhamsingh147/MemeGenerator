package com.example.mgen;



import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	@Override
	public void onBackPressed() {
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	} 
	
}