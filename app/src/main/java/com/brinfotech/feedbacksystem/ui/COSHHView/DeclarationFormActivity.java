package com.brinfotech.feedbacksystem.ui.COSHHView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.ui.signatureView.SignatureViewActivity;

import java.io.File;

import butterknife.BindView;

public class DeclarationFormActivity extends BaseActivity {

    @BindView(R.id.edtFullName)
    EditText edtFullName;

    @BindView(R.id.edtDateCompleted)
    EditText edtDateCompleted;

    @BindView(R.id.edtLocation)
    EditText edtLocation;

    @BindView(R.id.edtPinNo)
    EditText edtPinNo;

    @BindView(R.id.txtSignature)
    TextView txtSignature;

    @BindView(R.id.imgSignature)
    ImageView imgSignature;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    private static final int REQUEST_SIGN_OUT_SIGNATURE = 1001;
    private File file1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgSignature.setOnClickListener(this::onClick);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_declaration_form_activity;
    }

    @Override
    public void onClick(View view) {

        if(view==imgSignature){
            Intent intent = new Intent(DeclarationFormActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_OUT_SIGNATURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_OUT_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");

            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imgSignature.setImageBitmap(bitmap);
            txtSignature.setVisibility(View.GONE);

            file1 = new File(filePath);
        }
    }
}
