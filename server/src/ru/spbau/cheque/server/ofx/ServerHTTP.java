package ru.spbau.cheque.server.ofx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * NetBeans
 *
 * @author sam
 */
public class ServerHTTP {

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080); // создаем сокет сервера и привязываем его к порту 8080
        System.err.println("Ready!");
        while (true) {
            Socket s = ss.accept(); // заставляем сервер ждать подключений
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start(); // Работаем в отдельном потоке
        }
    }

    private static class SocketProcessor implements Runnable {

        private Socket s; // текущий сокет
        private InputStream is; // входной поток
        private OutputStream os; // Выходной поток

        private SocketProcessor(Socket s) throws Throwable { // Берем входной и выходной потоки сокета
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        @Override // переопределяем
        public void run() {
            try {
                readInputHeaders(); //Получили запрос
                ExportOFX OFX = new ExportOFX(1);//TODO: Get UserID from client
                writeResponse(OFX.toString()); // Отправили ответ
            } catch (Throwable t) {
                /*
                 * do nothing
                 */
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*
                     * do nothing
                     */
                }
            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" // выводим служебную информацию
                    + "Server: Cheque Recognizer HTTP Server/2012-04-20\r\n"
                    + "Content-Type: text/html;charset=UTF-8\r\n"
                    + "Content-Length: " + s.length() + "\r\n"
                    + "Connection: close\r\n\r\n";
            String result = response + s; // выводим полезную информацию
            os.write(result.getBytes()); // пишим в поток
            os.flush(); // заставляем поток закончить передачу данных.
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String str = br.readLine();
                if (str == null || str.trim().length() == 0) {
                    break;
                } else {
                    //GET method
                }
            }
        }
    }
}