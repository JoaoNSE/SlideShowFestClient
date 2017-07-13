package socket.teste.joao.socketteste;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by joao on 11/07/17.
 */
public class FotoClient extends AsyncTask<Void, Void, String> {

    private Integer port = 5000;
    private String ip;
    private Socket cliente;

    private Bitmap bitmap;
    private File file;

    public FotoClient(String ip, Bitmap bitmap) {
        this.bitmap = bitmap;

        this.ip = ip;
    }

    public FotoClient(String ip, File file) {
        this.file = file;

        this.ip = ip;
    }

    @Override
    protected String doInBackground(Void... params) {
        String ret = "Deu certo";
        Log.w("CONEXÃO", "Começou a thread");
        try {

            cliente = null;

            cliente = new Socket(ip, port);

            //Convertendo foto para ByteArray

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            int start = 0;
            int len = byteArray.length;

            OutputStream out = cliente.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            //dos.write(byteArray, start, len);
            Log.w("TAMANHO", ""+len);

            //dos.writeByte(len);
            dos.write(byteArray);
            //dos.writeBytes("Oi Enoque");

            dos.flush();
            dos.close();


        } catch (Exception e) {
            //FIXME Tratar a Exception.
            e.printStackTrace();
            return "Deu erro";
        } finally {
            if (cliente != null) {
                try {

                    cliente.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    protected void onProgressUpdate(Integer... progress) {
        Log.w("ENVIANDO", "enviando imagem");
    }

    protected void onPostExecute(Long result) {
        Log.w("ENVIANDO", "imagem enviada");
    }

}
