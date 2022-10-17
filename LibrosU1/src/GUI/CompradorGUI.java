package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Agentes.Comprador;
import java.awt.FlowLayout;

public class CompradorGUI extends JFrame {

    private Comprador miAgente;

    private JTextField titleField;

    public CompradorGUI(Comprador a) {
        super(a.getLocalName());

        miAgente = a;

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Titulo del libro:"));
        titleField = new JTextField(15);
        p.add(titleField);
        getContentPane().add(p, BorderLayout.CENTER);

        JButton addButton = new JButton("Tratar de comprar");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    String title = titleField.getText().trim();
                    miAgente.intentaComprar(title);
                    close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CompradorGUI.this, "Invalid values", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);
        setResizable(false);
    }

    public void showGui() {
        this.setSize(350, 150);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;

        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }

    private void close() {
        this.dispose();
    }
}
