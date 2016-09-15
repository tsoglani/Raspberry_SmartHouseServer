
/**
 * Write a description of class TimerView here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
public class TimerView extends JPanel
{
    private Fr fr;
    private Thread thread;
    public TimerView(Fr fr)
    {
        this.fr=fr;
        fr.isTimerModeSelected=true;
        showList();

        thread=  new Thread(){public void run(){
                while(fr.isTimerModeSelected){
                    try{ 
                        sleep(1000);
                        updateTimers();
                    }catch(Exception e){
                        e.printStackTrace();
                    }}

            }};
        thread.start();
    }

    ArrayList <MyJPanel> switcButtons;

    private void showList(){
        fr.getContentPane().removeAll();
        setLayout(new BorderLayout());
        JPanel header=new JPanel();
        JPanel center=new JPanel();
        switcButtons=new <MyJPanel> ArrayList();

        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_START);
        add(header,BorderLayout.PAGE_START);
        ArrayList<String>[] usingList;
        String neededOutputs;

        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();

        String [] outputs=neededOutputs.split("@@@");

        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            switcButtons.add(button);
            center.add(button);
            add(center);
        }
        updateTimers();
        repaint();
        revalidate();
        fr.add(this);
        fr.repaint();
        fr.revalidate();
    }

    static ArrayList<TimerCountdown> timerVisible= TimerCountdown.timers;

    private void updateTimers(){
        ArrayList<TimerCountdown> timers= TimerCountdown.timers;
        for(int i=0;i<timers.size();i++){

            //   if(timerVisible.contains(timers.get(i))){
            //   continue;
            //    }
            TimerCountdown timer=timers.get(i);
            String timerFullCommand=timer.commandText;
            int timerSecondsRemain=timer.seconds;
            boolean isGoingToOpen=false;
            String timerCommand=null;

            if(timerFullCommand.endsWith("on")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"on".length());
                isGoingToOpen=true;

            }else if(timerFullCommand.endsWith("off")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"off".length());
                isGoingToOpen=false;
            }

            for(int j=0;j<switcButtons.size();j++){
                MyJPanel myPanel=switcButtons.get(j);
                if(myPanel.title.equals(timerCommand)){
                    myPanel.timers.add(timer);
                    //        String textToButton ;
                    //      if(isGoingToOpen){
                    //      textToButton="open";}else{
                    //            textToButton="close";
                    //     }
                    //  JButton button=new JButton(textToButton+" in "+timerSecondsRemain+" seconds");

                    //   myPanel.addCenter(button);
                }
            }
            System.out.println(timer);
        }

        for(int j=0;j<switcButtons.size();j++){
            MyJPanel myPanel=switcButtons.get(j);
            myPanel.update();
        }

        timerVisible= TimerCountdown.timers;
    }

    private class MyJPanel extends JPanel{
        String title ;
        ArrayList <TimerCountdown> timers=new ArrayList  <TimerCountdown>();
        private JPanel centerPanel= new JPanel();
          JScrollPane scrollSpecific;
        private  ArrayList<CostumeButton> buttons = new  ArrayList<CostumeButton>();
            ArrayList <CostumeButton> removingButtons= new ArrayList<CostumeButton>();
                ArrayList <TimerCountdown> removingTimers= new ArrayList<TimerCountdown>();
        public MyJPanel(String title){
            this.title=title;
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
              scrollSpecific = new JScrollPane(centerPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            add(titlePanel,BorderLayout.PAGE_END);
            add(scrollSpecific,BorderLayout.CENTER);

        }
    
        public void update(){
            removingButtons.removeAll(removingButtons);
            removingTimers.removeAll(removingTimers);
            // System.out.println();

            for(int i=0;i<timers.size();i++){
                TimerCountdown timer=timers.get(i);
                boolean contains=false;

                 if(!TimerCountdown.timers.contains(timer)){
                        // remove button
                        for(int j=0;j<buttons.size();j++){
                            CostumeButton cb=buttons.get(j);
                            if(cb.id==timer.id){

                                removingButtons.add(cb);
                                removingTimers.add(timer);
                                System.out.println("remove timer");
                            }
                        }
                       
                        continue;
                    } 
                for(int j=0;j<buttons.size();j++){
                    if(buttons.get(j).id==timer.id){
                        contains=true;
                    }
                }

                if(contains){
                    // update text

                    for(int j=0;j<buttons.size();j++){
                        CostumeButton cb=buttons.get(j);
                        if(cb.id==timer.id){

                            cb.setText(getTextToButton(timer));


                        }
                    }

                   
                }else{
                    // create button and add it 
                    CostumeButton button = new CostumeButton(timer);
                    buttons.add(button);
                    timers.add(timer);
                    button.setText(getTextToButton(timer));
                    addCenter(button);

                }

            }

            if(!removingButtons.isEmpty()){

                for(int i=0;i<removingButtons.size();i++){
                    CostumeButton cb=removingButtons.get(i);
                    centerPanel.remove(cb);
                    buttons.remove(cb);
                    centerPanel.repaint();
                    centerPanel.revalidate();
                    System.out.println("remove "+cb.getText());
                }

            }
            
            if(!removingTimers.isEmpty()){

                for(int i=0;i<removingTimers.size();i++){
                    TimerCountdown tm=removingTimers.get(i);
                    timers.remove(tm);

                }

            }
        }

        private String getTextToButton(TimerCountdown timer){
            String textToButton ;
            String timerFullCommand=timer.commandText;
            int timerSecondsRemain=timer.seconds;
            boolean isGoingToOpen=false;
            String timerCommand=null;

            if(timerFullCommand.endsWith("on")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"on".length());
                isGoingToOpen=true;

            }else if(timerFullCommand.endsWith("off")){
                timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"off".length());
                isGoingToOpen=false;
            }

            if(isGoingToOpen){
                textToButton="open";}else{
                textToButton="close";
            }
            return textToButton+" in "+timerSecondsRemain+" seconds";
        }

        private void addCenter(Component c){

            centerPanel.add(c);
            centerPanel.repaint();
            centerPanel.revalidate();

        }

    }
    private class CostumeButton extends JButton{
        int id;
        TimerCountdown timer;
        public CostumeButton(TimerCountdown timer){

            this.timer=timer;
                        this.id=timer.id;
                                    setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }

}