/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
/**
 *
 * @author Administrator
 */
//class MessageBox extends Frame implements ActionListener{
class MessageBox extends Dialog implements ActionListener {
    Label msgLabel;
    Button closeButton;

    public MessageBox(JFrame f, String title, String msg) {
        super(f, title, true);
        msgLabel = new Label(msg);
        closeButton = new Button("Close");
        closeButton.addActionListener(this);

        setLayout(new FlowLayout());

        add(msgLabel);
        add(closeButton);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        dispose();
    }
}
