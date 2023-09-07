package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Ventana extends JFrame{
    JPanel panel2 = new JPanel();
    JButton boton1 = new JButton("Compilar");
    JButton boton2 = new JButton("Nuevo");
    TextArea srcCode = new TextArea();

    public void Ventanita(){
        setTitle("Compilador");
        setSize(750,650);
        setVisible(true);
        setLocationRelativeTo(null);
        Panel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void Panel(){
        this.getContentPane().add(panel2);
        panel2.setBackground(new Color(99, 11, 40));
        panel2.setLayout(null);
        boton1();
        boton2();
        srcCode();
    }
    
    public String srcCode() {
        srcCode.setBounds(75, 50, 600, 450);
        Font font = new Font("DialogInput", Font.PLAIN, 20);
        srcCode.setFont(font);
        srcCode.setEditable(true);
        panel2.add(srcCode);

        return srcCode.getText();
    }

    public void boton1(){
        boton1.setBounds(475, 525, 200, 50);
        Font font = new Font("DialogInput", Font.PLAIN, 18);
        boton1.setFont(font);
        panel2.add(boton1);

        ActionListener accion = e -> {
            System.out.println("\n");
            Funciones x =new Funciones();
            try {
                x.secretaria(srcCode());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        boton1.addActionListener(accion);
    }

    public void boton2(){
        boton2.setBounds(75, 525, 200, 50);
        Font font = new Font("DialogInput", Font.PLAIN, 18);
        boton2.setFont(font);
        panel2.add(boton2);

        ActionListener accion = e -> srcCode.setText(null);
        boton2.addActionListener(accion);
    }

}
