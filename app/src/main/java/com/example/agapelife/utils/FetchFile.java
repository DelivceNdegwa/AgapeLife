package com.example.agapelife.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FetchFile {
    int FILE_ACTION_CODE;
    Context context;

    public FetchFile(int FILE_ACTION_CODE, Context context) {
        this.FILE_ACTION_CODE = FILE_ACTION_CODE;
        this.context = context;
    }

    public boolean verifyPermissions(){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
        };

        boolean isPermitted = false;

        if(
                ContextCompat.checkSelfPermission(
                        context.getApplicationContext(), permissions[0])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        context.getApplicationContext(), permissions[1])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        context.getApplicationContext(), permissions[2])
                        == PackageManager.PERMISSION_GRANTED
        ){
            isPermitted = true;
        }
        else{
            ActivityCompat.requestPermissions((Activity) context, permissions, 134);
        }

        return isPermitted;
    }

    public boolean pickDoc(){
        verifyPermissions();
        boolean response = true;

        Intent pickDocument = new Intent(Intent.ACTION_GET_CONTENT);
        pickDocument.setType("application/*");
        pickDocument.addCategory(Intent.CATEGORY_OPENABLE);

        String[] mimeTypes = {
                "application/pdf",
                "application/xls",
                "application/docx",
                "application/doc",
        };

        pickDocument.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        try{
//            ActivityCompat.startActivityForResult(pickDocument, FILE_ACTION_CODE);

        }
        catch (ActivityNotFoundException e){
            Toast.makeText(context, "No application found to open files", Toast.LENGTH_SHORT).show();
            response = false;
        }

        return response;

    }
}
