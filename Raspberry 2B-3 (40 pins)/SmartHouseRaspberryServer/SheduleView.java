import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
public class SheduleView extends JPanel
{ private JButton back;
    private Fr fr;
    private  Color []colors = {Color.gray,Color.green,Color.red};
    ArrayList   <MyJPanel> myPanels;
    public SheduleView(Fr fr)
    {

        this.fr=fr;
        setLayout(new BorderLayout());
        //    ImageIcon backTime=new ImageIcon("back.png");
        //   backTime=new ImageIcon(fr.getScaledImage(backTime.getImage(),(int)(fr.height/15), (int)(fr.height/15)));
        //      back = new JButton(backTime);
        //             back = new JButton(backTime);
        createGUI();
        fr.isSheduleModeSelected=true;
        fr.shv=this;

        new Thread(){
            public void run(){
                while(fr.isSheduleModeSelected)   {
                    try{
                       update(fr.sh.db.getShedules());
                        Thread.sleep(2000);
                    }catch(Exception e){}}
            }}.start();
    }

    protected void update(ArrayList<Shedule> sheduleList){
        if(sheduleList!=null)
            for(int i=0;i<sheduleList.size();i++){
                Shedule shedule=sheduleList.get(i);
                boolean containsInMyPanels=false;
                boolean isGoingToOpen;
                String timerFullCommand=shedule.getCommandText();
                String timerCommand=null;
                if(timerFullCommand.endsWith("on")){
                    timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"on".length());
                    isGoingToOpen=true;

                }else if(timerFullCommand.endsWith("off")){
                    timerCommand=timerFullCommand.substring(0,timerFullCommand.length()-1-"off".length());
                    isGoingToOpen=false;
                }
                if(myPanels!=null)
                    for(int j=0;j<myPanels.size();j++){
                        MyJPanel mp=myPanels.get(j);

                        if(mp.title.equals(timerCommand)){
                            System.out.println(timerCommand+":::"+mp.title);
                            /////for 
                            mp.update(shedule,sheduleList);

                        }
                    }
            }

    }

    private void createGUI(){
        fr.getContentPane().removeAll();
        fr.getContentPane().add(this);

        JPanel header=new JPanel();
        // JPanel bottom=new JPanel();
        // bottom.setLayout(new BorderLayout());

        header.setLayout(new BorderLayout());
        header.add(fr.home,BorderLayout.LINE_END);
        //  header.add(back,BorderLayout.LINE_START);

        JPanel center=new JPanel();
        myPanels=new <MyJPanel> ArrayList();
        add(header,BorderLayout.PAGE_START);
        // add(bottom,BorderLayout.PAGE_END);
        ArrayList<String>[] usingList;
        String neededOutputs;
        usingList=fr.sh.outputPowerCommands;
        neededOutputs=fr.sh.getAllCommandOutput();
        String [] outputs=neededOutputs.split("@@@");
        center.setLayout(new GridLayout((int)Math.sqrt(usingList.length)+1,(int)Math.sqrt(usingList.length)+1));
        for(int i=0;i<usingList.length;i++){
            ArrayList <String> list=usingList[i];
            MyJPanel button = new MyJPanel(list.get(0));
            myPanels.add(button);
            center.add(button);
            add(center);
        }

        repaint();
        revalidate();
        fr.repaint();
        fr.revalidate();

    }
    private class MyJPanel extends JPanel{
        int color_id=0;
        String title ;
        protected JPanel centerPanel= new JPanel();
        private JScrollPane scrollSpecific;
        public MyJPanel(String title){
            this.title=title;
            scrollSpecific = new JScrollPane(centerPanel);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel titleLabel= new JLabel(title);
            JPanel titlePanel= new JPanel();
            titlePanel.add(titleLabel);
            setBackground(colors[color_id]);
            titlePanel.setBackground(colors[color_id]);
            add(titlePanel,BorderLayout.PAGE_START);
            //  add(centerPanel);
            add(scrollSpecific,BorderLayout.CENTER);
            centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
            // example of use
            //   for(int j=0;j<10;j++){
            //     SingleSheduleView ssv=new SingleSheduleView();
            // centerPanel.add(ssv);
            // }
            //## end of example

        }
        ArrayList<SingleSheduleView> deletedSingleSheduleView = new ArrayList<SingleSheduleView>();
        protected void update(Shedule shedule,ArrayList<Shedule> sheduleList){
            boolean contains=false;
            SingleSheduleView usingSsv=null;
            deletedSingleSheduleView.removeAll(deletedSingleSheduleView);
            for(int i=0;i<centerPanel.getComponentCount();i++){
                SingleSheduleView ssv=(SingleSheduleView)centerPanel.getComponent(i);
                boolean containsInSheduleList=false;
                for(int j=0;i<sheduleList.size();j++){
                    Shedule shedule2=sheduleList.get(j);
                    if(shedule2.getId()==ssv.id){
                        containsInSheduleList=true;
                        break;
                    }

                }
                if(!containsInSheduleList){
                    deletedSingleSheduleView.add(ssv);
                }
                if(ssv.id==shedule.getId()){
                    contains=true;
                    usingSsv=ssv;
                }
            }
            if(!contains){
                centerPanel.add(new SingleSheduleView(shedule));
            }else{
                if(usingSsv!=null){
                    usingSsv.updateAll(shedule);
                }

            }
            for(int i=0;i<deletedSingleSheduleView.size();i++){
                centerPanel.remove(deletedSingleSheduleView.get(i));
            }

            /// check for shedule in centerPanel id if not exist create
            /// if exist update text

        }
        private class SingleSheduleView extends JPanel{

            private String activeDays, time, weeklyString, activeString,commandString;
            protected  int id;
            protected JPanel firstRow,secondRow;
            private JButton delete,edit;
            private JCheckBox isWeekly,isActive; 
            private JLabel [] days= new JLabel[7];
            private Shedule shedule;
            private JPanel centerPanel,header;
            protected JLabel timeLabel;
            public SingleSheduleView(Shedule shedule){
                setLayout(new BorderLayout());
                activeDays= shedule.getActiveDays();
                weeklyString=  shedule.getIsWeekly();
                activeDays=  shedule.getActiveDays();
                activeString=  shedule.getIsActive();
                commandString=  shedule.getCommandText();
                centerPanel= new JPanel();
                header= new JPanel();
                id=shedule.getId();
                time=shedule.getTime();

                setBorder(BorderFactory.createLineBorder(Color.blue));
                firstRow= new JPanel();
                firstRow.setLayout (new GridLayout(1,7));
                secondRow= new JPanel();
                delete= new JButton("delete");
                edit= new JButton("edit");
                isWeekly= new JCheckBox("is Weekly");
                isActive= new JCheckBox("is isActive");
                centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
                centerPanel.add(firstRow);
                centerPanel.add(secondRow);
                timeLabel=new JLabel(time);
                header.add(timeLabel);
                add(centerPanel);
                add(header,BorderLayout.PAGE_START);
                for(int i=0;i<7;i++){
                    JLabel dayButton=null;
                    switch(i){
                        case 0:
                        dayButton= new JLabel("Su");
                        break;
                        case 1:
                        dayButton= new JLabel("Mo");
                        break;
                        case 2:
                        dayButton= new JLabel("Tu");
                        break;
                        case 3:
                        dayButton= new JLabel("We");
                        break;
                        case 4:
                        dayButton= new JLabel("Th");
                        break;
                        case 5:
                        dayButton= new JLabel("Fr");
                        break;
                        case 6:
                        dayButton= new JLabel("Sa");
                        break;    
                        default: 
                        dayButton= new JLabel("Uknown");

                    }
                    days[i]=dayButton;
               
                    firstRow.add(dayButton);
                    
                    dayButton.setBackground(colors[0]);
                    
                }
                secondRow.add(delete);

                secondRow.add(isWeekly);
                secondRow.add(isActive);
                secondRow.add(edit);

                updateAll(shedule);

            }

            private void updateAll(Shedule shedule){
                updateAll(shedule.getActiveDays(), shedule.getIsWeekly(), shedule.getIsActive(), shedule.getTime());
                repaint();
                revalidate();
                centerPanel.repaint();
                centerPanel.revalidate();
            }

            private void updateAll(String activeDays,String weeklyString,String activeString,String time){
                updateDaysEnable(activeDays);
                updateWeekly(weeklyString);
                updatActive(activeString);
                updateTime(time);
            }

            private void updateAll(){
                updateDaysEnable();
                updateWeekly();
                updatActive();
                updateTime();
            }

            protected void updateDaysEnable(String activeDays){
                this.activeDays=activeDays;
                updateDaysEnable();
            }

            protected void updateDaysEnable(){

                Color usingColor=colors[2];
                if(commandString.endsWith("on")){
                    usingColor=colors[1];
                }
                if(weeklyString.contains(Integer.toString( Calendar.SUNDAY))){
                    days[0].setBackground(usingColor);

                }
                if(weeklyString.contains(Integer.toString( Calendar.MONDAY))){
                    days[1].setBackground(usingColor);
                }
                if(weeklyString.contains(Integer.toString( Calendar.TUESDAY))){
                    days[2].setBackground(usingColor);
                }
                if(weeklyString.contains(Integer.toString( Calendar.WEDNESDAY))){
                    days[3].setBackground(usingColor);
                }
                if(weeklyString.contains(Integer.toString( Calendar.THURSDAY))){
                    days[4].setBackground(usingColor);
                }
                if(weeklyString.contains(Integer.toString( Calendar.FRIDAY))){
                    days[5].setBackground(usingColor);
                }
                if(weeklyString.contains(Integer.toString( Calendar.SATURDAY))){
                    days[6].setBackground(usingColor);
                }
//setBackground(usingColor);
//centerPanel.setBackground(usingColor);
//firstRow.setBackground(usingColor);
//secondRow.setBackground(usingColor);

            }

            protected void updateTime(String time){
                this.time=time;
                updateWeekly();
            }

            protected void updateTime(){
                timeLabel.setText(time);
            }

            protected void updateWeekly(String weeklyString){

                this.weeklyString=weeklyString;
                updateWeekly();
            }

            protected void updateWeekly(){
                if(weeklyString.equalsIgnoreCase("true")|| weeklyString.equalsIgnoreCase("enable")){
                    isWeekly.setSelected(true);
                }else  if(weeklyString.equalsIgnoreCase("false")|| weeklyString.equalsIgnoreCase("disable")){
                    isWeekly.setSelected(false);
                }
            }

            protected void updatActive(String activeString){

                this.activeString=activeString;
                updatActive();
            }

            protected void updatActive(){

                if(activeString.equalsIgnoreCase("true")|| activeString.equalsIgnoreCase("enable")){
                    isActive.setSelected(true);
                }else  if(activeString.equalsIgnoreCase("false")|| activeString.equalsIgnoreCase("disable")){
                    isActive.setSelected(false);
                }
            }

        }

    } 

}
