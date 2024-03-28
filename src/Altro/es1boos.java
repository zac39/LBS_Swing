package Altro;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author byzac
 */
public class es1boos extends JFrame implements ActionListener{

    private JLabel jl1, jl2, jl3;    
    private JButton jb[], jbs;
    private int m[];
    private JPanel p, p2, p3;    
    private int motta;
    private int tentativi, premuto;
    private boolean fine;
    
    public es1boos(String title) {

        super(title);        
        JOptionPane.showMessageDialog(null, "Ciao e benvenuto nel gioco delle marmotte. Trova tutte le marmotte per vincere, le marmotte sono 4 e hai 6 tentativi."
                + "\n Per giocare devi premere i pulsanti in basso, in caso ci sia una marmotta comparirà (marmotta) sul tasto in caso contrario comparirà una (X)");

        jl1 = new JLabel("Marmotte trovatre:");
        jl2 = new JLabel("Tentativi: ");         
        jl3 = new JLabel("");        
        m=new int[12];
        jb=new JButton[12];                
        p = new JPanel();
        p2 = new JPanel();        
        p3 = new JPanel();        
        jbs=new JButton();
        fine=false;
        
        Icon ic = new ImageIcon("icon/marmotta.png");
        jbs.setIcon(ic);
        jbs.addActionListener(this);
        jbs.setName("angl");
        
        for (int i=0; i<m.length; i++){
            m[i]=0;
        }
        
        for (int i=0; i<4; i++){
            int r=(int)(Math.random()*12);
            while(m[r]==1){
                r=(int)(Math.random()*12);
            }
            m[r]=1;
        }        
        
        setLayout(new GridLayout(2,0));        
        setSize(400, 500);
        p.setLayout(new GridLayout(3,4));      
        p2.setLayout(new GridLayout(0,2));
        p3.setLayout(new GridLayout(2,0));        
        
        for (int i=0; i<m.length; i++) {
             jb[i] = new JButton("?");
             jb[i].setName(i+"");
            jb[i].addActionListener(this);
            p.add(jb[i]);// memorizza i pulsanti
        }        
        
        p3.add(jl2);
        p3.add(jl1);        
        p2.add(p3);
        p2.add(jbs);
        add(p2);        
        add(p);     
        
        this.setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
         
    public void chiudi(){
         dispose();
        }   
   
         
         @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton){
            premuto=premuto+1;
            JButton b= (JButton) e.getSource();
            if(b.getName().equals("angl")!=true){
            int intB =Integer.parseInt(b.getName());
            if(m[intB]== 1){
            motta=motta+1;
            jl1.setText("Marmotte trovatre: "+motta);
            jb[intB].setText("Marmotta");
            jb[intB].setBackground(Color.green);
            }
            else{
                jb[intB].setText("X");
                jb[intB].setBackground(Color.red);
            }
            jb[intB].removeActionListener(this);
            tentativi=tentativi+1;
            jl2.setText("tentativi: "+tentativi);
            }
            
            if (tentativi==6 && b.getName().equals("angl")){
                Icon ic = new ImageIcon("icon/mlmlmlmlm.jpg");
                jbs.setIcon(ic);
            }

            if (premuto==20 && b.getName().equals("angl")){
                //Altro.tuMa snas = new Altro.tuMa();
                chiudi();
            }            
        }

        if (tentativi==6 && fine==false){
            for (int i=0; i<m.length; i++) {
                jb[i].removeActionListener(this);
            }
            
            if(motta==4){
                JOptionPane.showMessageDialog(null, "Il giocho è finito, hai vinto hai trovato tutte e 4 le marmotte");
            }
            else{
                JOptionPane.showMessageDialog(null, "Il giocho è finito, hai perso. Hai trovato "+ motta+" marmotte");   
            }
            fine=true;
        }
    }
    
    public static void main(String[] args) {
        es1boos j1 = new es1boos("Le marmotte mi hanno ispirato");
    }
}
