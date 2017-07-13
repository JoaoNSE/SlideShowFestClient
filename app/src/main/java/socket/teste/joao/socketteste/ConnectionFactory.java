package socket.teste.joao.socketteste;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Created by joao on 28/06/17.
 */
public class ConnectionFactory extends AsyncTask<Void, Void, String> {
    private Integer port = 34567;
    private String ip;
    private Socket cliente;

    private String conexao = "Não Conectado";

    private PrintStream saida;
    private InputStream entrada;

    private TextView respostaTv;

    private Scanner s;

    private String msg = null;
    private String retorno;

    public ConnectionFactory(TextView respostaTv, String msg, String ip) {
        this.msg = msg;
        this.respostaTv = respostaTv;
        this.ip = ip;
    }

    @Override
    protected String doInBackground(Void... params) {
        String ret = "Erro";
        try {
            cliente = null;
            saida = null;
            entrada = null;

            cliente = new Socket(ip, port);

            saida = new PrintStream(cliente.getOutputStream());
            saida.println(msg);

            entrada = cliente.getInputStream();
            Scanner s = new Scanner(entrada);

            ret = s.nextLine();

            s.close();

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

    @Override
    protected void onPostExecute(String result) {
        respostaTv.setText(result);
        super.onPostExecute(result);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getConexao() {
        return this.conexao;
    }

    public String getRetorno() {
        return this.retorno;
    }
}
