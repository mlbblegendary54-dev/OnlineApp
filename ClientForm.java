import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;
 
public class ClientForm extends javax.swing.JFrame {
 
    private static final Logger logger = Logger.getLogger(ClientForm.class.getName());
 
    private PrintWriter out;
    private Socket socket;
    private String username;
    private volatile boolean running = false;
 
    public ClientForm(String username) {
        this.username = username;
        initComponents();
        setTitle("Client Chat - " + username);
 
        // === GẮN SỰ KIỆN NÚT ===
        btnSend.addActionListener(e -> sendMessage());
 
        // Nhấn Enter trong ô nhập để gửi
        txtMessage.addActionListener(e -> sendMessage());
 
        // Xóa placeholder text
        txtMessage.setText("");
 
        // Kết nối đến server
        connectToServer();
    }
 
    // Constructor mặc định (chạy standalone)
    public ClientForm() {
        this("Client");
    }
 
    private void connectToServer() {
        new Thread(() -> {
            try {
                appendMessage("[Hệ thống] Đang kết nối đến server...");
                socket = new Socket("localhost", ServerForm.PORT);
                running = true;
                appendMessage("[Hệ thống] Đã kết nối đến server!");
 
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
 
                String line;
                while ((line = in.readLine()) != null) {
                    appendMessage("Server : " + line);
                }
            } catch (IOException e) {
                if (running) {
                    appendMessage("[Hệ thống] Mất kết nối với server.");
                } else {
                    appendMessage("[Hệ thống] Không thể kết nối đến server: " + e.getMessage());
                }
            }
        }, "ClientThread").start();
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
        if (out != null && running) {
            out.println(msg);
            appendMessage("Client : " + msg);
            txtMessage.setText("");
            txtMessage.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Chưa kết nối đến server!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDisplay = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("CLIENT");

        txtMessage.setText("jTextField1");

        btnSend.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSend.setText("Send");

        txtDisplay.setColumns(20);
        txtDisplay.setRows(5);
        jScrollPane1.setViewportView(txtDisplay);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addGap(4, 4, 4))
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
        java.awt.EventQueue.invokeLater(() -> new ClientForm().setVisible(true));
    }
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtDisplay;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
