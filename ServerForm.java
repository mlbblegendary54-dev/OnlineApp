import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;
 
public class ServerForm extends javax.swing.JFrame {
 
    private static final Logger logger = Logger.getLogger(ServerForm.class.getName());
    public static final int PORT = 5000;
 
    private PrintWriter out;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private volatile boolean running = false;
 
    public ServerForm() {
        initComponents();
        setLocationRelativeTo(null);
 
        // === GẮN SỰ KIỆN NÚT ===
        btnSend.addActionListener(e -> sendMessage());
 
        // Nhấn Enter trong ô nhập để gửi
        txtMessage.addActionListener(e -> sendMessage());
 
        // Xóa placeholder text
        txtMessage.setText("");
 
        // Khởi động server socket
        startServer();
    }
 
    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                running = true;
                appendMessage("[Hệ thống] Server đang chờ kết nối trên cổng " + PORT + "...");
 
                clientSocket = serverSocket.accept();
                appendMessage("[Hệ thống] Client đã kết nối!");
 
                out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
 
                String line;
                while ((line = in.readLine()) != null) {
                    appendMessage("Client : " + line);
                }
            } catch (IOException e) {
                if (running) {
                    appendMessage("[Hệ thống] Kết nối bị ngắt.");
                }
            }
        }, "ServerThread").start();
    }
 
    private void appendMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            txtDisplay.append(msg + "\n");
            txtDisplay.setCaretPosition(txtDisplay.getDocument().getLength());
        });
    }
 
    private void sendMessage() {
        String msg = txtMessage.getText().trim();
        if (msg.isEmpty()) return;
        if (out != null) {
            out.println(msg);
            appendMessage("Server : " + msg);
            txtMessage.setText("");
            txtMessage.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Chưa có client kết nối!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDisplay = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("SERVER");

        txtDisplay.setColumns(20);
        txtDisplay.setRows(5);
        jScrollPane1.setViewportView(txtDisplay);

        txtMessage.setText("jTextField1");

        btnSend.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSend.setText("Send");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(txtMessage))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new ServerForm().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtDisplay;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
