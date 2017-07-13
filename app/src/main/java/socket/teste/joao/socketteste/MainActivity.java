package socket.teste.joao.socketteste;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    EditText ipEt;

    ImageView fotoIv;

    String pictureImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ipEt = (EditText) findViewById(R.id.ipEt);
        fotoIv = (ImageView) findViewById(R.id.fotoIV);
    }

    public void cosdffnnect(View view) {
//        ConnectionFactory fc = new ConnectionFactory();
//        String cn = fc.getConexao();
//        conexaoSt.setText(cn);
//        if (!cn.equals("Não Conectado")) {
//          //  fc.run();
//        }
    }

    public void tirarFoto(View view) {
        //Intent tiraFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(tiraFoto, 1234);

        //CEHCA SE TEM PERMISSÃO PARA LER ARQUIVOS
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = timeStamp + ".jpg";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
////        File storageDir = new File(Environment.getExternalStorageDirectory(), imageFileName);
//
//        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
////        pictureImagePath = storageDir.getAbsolutePath();
//        Log.w("URI DA FOTO", storageDir.getAbsolutePath());
//
//        File file = new File(pictureImagePath);
//
////        try {
////            file = createImageFile();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        Uri outputFileUri = Uri.fromFile(file);
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//        startActivityForResult(cameraIntent, 1234);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1234);
            } else {
                Log.w("FOTO", "FIle é nulo");
            }
        } else {
            Log.w("FOTO", "Alguma coisa ta nula");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1234) {
            if (resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imagem = (Bitmap) extras.get("data");
//            fotoIv.setImageBitmap(imagem);
//            FotoClient client = new FotoClient(ipEt.getText().toString(), imagem);
//            client.execute();

                File imgFile = new File(pictureImagePath);
                if (imgFile.exists()) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        Log.w("PERMISSÃO", "Permissão foi negada");

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                123);

                    } else {
//                    FotoClient client = new FotoClient(ipEt.getText().toString(), imgFile);
//                    client.execute();
                        Log.w("CONEXÃO", "Antes de conectar e de pegar a imagem");
                        Bitmap imagem = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        FotoClient client = new FotoClient(ipEt.getText().toString(), imagem);

                        int imgW = fotoIv.getWidth();
                        int imgH = fotoIv.getHeight();

                        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(imagem, imgW, imgH);

                        fotoIv.setImageBitmap(thumbnail);
                        client.execute();

                    }
                }
            } else {
                Log.w("FOTO", "algo deu errado na foto");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = image.getAbsolutePath();
        return image;

    }
}
