import java.io.IOException;
import java.util.Queue;

public class dispatcher extends Thread{
    public dispatcher(){

    }
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Time "+OS.Time + " READY Queue in "+OS.readyQueue);
            System.out.println("---------------------------------------");
            if(OS.readyQueue.isEmpty()==false){
                Process p = OS.readyQueue.poll();
                System.out.println("Current running process "+p);
                System.out.println("---------------------------------------");
                try {
                    p.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(p.state != States.FINISHED && p.state!=States.BLOCKED) {
                    p.state = States.READY;
                    OS.readyQueue.add(p);
                }
            }
            else{
                OS.flag = false;
                break;
            }
        }
    }
}
