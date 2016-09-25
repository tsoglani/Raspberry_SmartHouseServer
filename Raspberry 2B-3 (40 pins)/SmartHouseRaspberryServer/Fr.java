
/**
 * Write a description of class Fr here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.*;
public class Fr extends JFrame
{
    // instance variables - replace the example below with your own
    private JButton manual;
    private JButton timer;
    private JButton auto;
    protected boolean isSwitchModeSelected=false;
    protected boolean isTimerModeSelected=false;
       protected boolean isSheduleModeSelected=false;
    private String state="off";
    protected JButton home;
    protected SH sh;
    protected  SheduleView shv;
    JToggleButton toggle;
    /**
     * Constructor for objects of class Fr
     */
    double width,height;
    public Fr(SH sh)
    {
        this.sh=sh;
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        width=screenSize.getWidth();
        height=screenSize.getHeight();

        ImageIcon homeIcon=new ImageIcon("home.png");
        homeIcon=new ImageIcon(getScaledImage(homeIcon.getImage(),(int)(height/20), (int)(height/20)));
        
        
        ImageIcon manualIcon=new ImageIcon("manual.png");
        manualIcon=new ImageIcon(getScaledImage(manualIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        
        manual= new JButton(manualIcon);
      toggle=new JToggleButton("Commands Mode");
         ImageIcon timelIcon=new ImageIcon("time.png");
        timelIcon=new ImageIcon(getScaledImage(timelIcon.getImage(),(int)(width/4), (int)(3*height/5)));
      timer= new JButton(timelIcon);
               ImageIcon automationIcon=new ImageIcon("automation.png");
        automationIcon=new ImageIcon(getScaledImage(automationIcon.getImage(),(int)(width/4), (int)(3*height/5)));
        auto= new JButton(automationIcon);
        home= new JButton(homeIcon);
        home.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    mainMenu();
                    isSwitchModeSelected=false;
                    isTimerModeSelected=false;
                    isSheduleModeSelected=false;
                    shv=null;
                    toggle.setText("Commands Mode");
                }
            });

        timer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new TimerView(Fr.this);
                }
            });
            
              auto.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                  
               new SheduleView(Fr.this);
                }
            });

        this.setSize((int)width,(int)height);
        mainMenu();
    //    setLocation(0,0);
   //     setUndecorated(true);
    //    getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    
        setVisible(true);
    }

    private void mainMenu(){
        getContentPane().removeAll();
        setLayout(new GridLayout(1,3));
        add(manual);
        add(timer);
        add(auto);
        manual.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    manualSelected();
                }
            });

        repaint();
        revalidate();
    }

    ArrayList <JButton> switcButtons;
    private void manualSelected(){
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        JPanel header=new JPanel();
        JPanel center=new JPanel();
        switcButtons=new <JButton> ArrayList();
        isSwitchModeSelected=true;
        toggle.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    if(!toggle.isSelected()){

                        toggle.setText("Commands Mode");
                        manualSelected();
                    }else{
                        toggle.setText("Output Mode");
                        manualSelected();

                    }
                }
            });
        header.setLayout(new BorderLayout());
        home.setSize((int)(height/20), (int)(height/20));
        header.add(home,BorderLayout.LINE_START);
        header.add(toggle,BorderLayout.LINE_END);
        add(header,BorderLayout.PAGE_START);

        ArrayList<String>[] usingList;
        String neededOutputs;
        if(toggle.getText().equals("Output Mode")){
            usingList=sh.outputCommands;
            neededOutputs=sh.getAllOutput();
        }else{
            usingList=sh.outputPowerCommands;
            neededOutputs=sh.getAllCommandOutput();
        }
        String [] outputs=neededOutputs.split("@@@");

        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            JButton button = new JButton(list.get(0));
            switcButtons.add(button);
            center.add(button);
            button.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){

             
                        if(button.getBackground()==Color.GREEN){state="off";}else{
                            state="on";
                        }

                        String command =button.getText();
                        new Thread(){public void run(){
                                try {
                                    if(toggle.getText().equals("Output Mode")){
                                        sh.processLedString(command + " " + state);
                                        sh.sendToAll("switch "+command + " " + state);
                                        System.out.println("pressed:"+command + " " + state);
                                        sh.sendTheUpdates(command + " " + state);
                                    }else{
                                        sh.processCommandString(command + " " + state);
                                        sh.sendToAll("switch "+command + " " + state);
                                        System.out.println("pressed:"+command + " " + state);
                                        sh.sendTheUpdates(command + " " + state);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }}.start();

                    }
                });
            for(int j=0;j<outputs.length;j++){
                boolean isOn=false;
                String textNeededFromOutput=null;
                if(outputs[j].endsWith("on")){
                    textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"on".length());
                    isOn=true;

                }else if(outputs[j].endsWith("off")){
                    textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"off".length());
                    isOn=false;
                }
                if(textNeededFromOutput!=null&&textNeededFromOutput.equals(button.getText())){
                    if(isOn){
                        button.setBackground(Color.GREEN);
                    }else{
                        button.setBackground(Color.GRAY);
                    }
                }
            }
        }
        add(center);

        repaint();
        revalidate();
    }

    protected void updateManual(){
        ArrayList<String>[] usingList;
        String neededOutputs;
        if(toggle.getText().equals("Output Mode")){
            usingList=sh.outputCommands;
            neededOutputs=sh.getAllOutput();
        }else{
            usingList=sh.outputPowerCommands;
            neededOutputs=sh.getAllCommandOutput();
        }
        String [] outputs=neededOutputs.split("@@@");

        for(int j=0;j<outputs.length;j++){
            System.out.println(outputs[j]);
            boolean isOn=false;
            String textNeededFromOutput=null;
            if(outputs[j].replaceAll(" ","").endsWith("on")){
                System.out.println(".endsWithon");
                textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"on".length());
                isOn=true;

            }else if(outputs[j].endsWith("off")){

                textNeededFromOutput=outputs[j].substring(0,outputs[j].length()-1-"off".length());
                isOn=false;
            }
            JButton button=null;
            if(switcButtons!=null){
                for(int i=0;i<switcButtons.size();i++){

                    if(switcButtons.get(i).getText().equals(textNeededFromOutput)){
                        button=switcButtons.get(i);

                    }
                }

                if(button!=null)
                    if(textNeededFromOutput!=null&&textNeededFromOutput.equals(button.getText())){

                        if(isOn){
                            System.out.println(button.getText()+":       changeState:"+textNeededFromOutput);
                            button.setBackground(Color.GREEN);
                        }else{
                            button.setBackground(Color.GRAY);
                        }
                    }
            }
        }
    }

    protected void changeState(String message){
        boolean isOn=false;
        String textNeededFromOutput=null;
        if(message.endsWith("on")){
            textNeededFromOutput=message.substring(0,message.length()-1-"on".length());
            isOn=true;

        }else if(message.endsWith("off")){
            textNeededFromOutput=message.substring(0,message.length()-1-"off".length());
            isOn=false;
        }

        if(switcButtons!=null){
            for(int i=0;i<switcButtons.size();i++){
                System.out.println(switcButtons.get(i).getText()+":       changeState:"+textNeededFromOutput);
                if(switcButtons.get(i).getText().equals(textNeededFromOutput)){
                    if(!isOn){
                        switcButtons.get(i).setBackground(Color.GRAY);
                    }else{
                        switcButtons.get(i).setBackground(Color.GREEN);
                    }

                }
            }}

    }

    protected Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
