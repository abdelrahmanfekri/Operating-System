import java.io.*;
import java.util.*;


public class OS extends Thread{
    static int timeSlice = 2;
    static Memory memory; // main memory
    static String path; // path to all files
    static Scanner input; // name of file
    static String programName;
    static Process[] src;
    static Queue<Process>[] srcQueue;
    static Queue<Process> readyQueue;
    static int Time =0;
    static int id =1;
    static boolean flag = true;
    public OS(){

    }
    public static Process readProcess(String path,int id) throws IOException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<String> inst = new ArrayList<>();
        int cnt = 1;
        while(sc.hasNextLine()) {
            String line = sc.nextLine(); // read a line from the file (instruction)
            StringTokenizer st = new StringTokenizer(line);
            String ist = st.nextToken();
            if(ist.equals("assign")){
                String var = st.nextToken();
                String check = st.nextToken();
                if(check.equals("input")){
                    inst.add("input");
                    inst.add("assign "+var);
                    cnt++;
                }
                else if(check.equals("readFile")){
                    inst.add("readFile "+st.nextToken());
                    inst.add("assign "+var);
                    cnt++;
                }else{
                    OS.memory.add("inputTmp"+(cnt-1), new Type(check), id);
                    inst.add("assign "+var);
                }
            }else{
                inst.add(line);
            }
            cnt++;
        }
        Process p = new Process(id,inst);
        p.state = States.NEW;
        return p;
    }
    public static void addNewProgram(String path){
        try {
            Process p = readProcess(path, id++);
            p.state = States.READY;
            readyQueue.add(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void add() {
        // this files path could be changed
        String firstProgramPath = "E:\\folder\\Semester6\\S6\\OS\\Project New\\OS_Project\\Program_1.txt";
        String secondProgramPath = "E:\\folder\\Semester6\\S6\\OS\\Project New\\OS_Project\\Program_2.txt";
        String thirdProgramPath = "E:\\folder\\Semester6\\S6\\OS\\Project New\\OS_Project\\Program_3.txt";
        if (Time == 0) {
                // read first program
            System.out.println("Time "+Time);
            System.out.println("first Program is added");
            System.out.println("---------------------------------------");
            addNewProgram(firstProgramPath);
        }
        else if (Time == 1) {
            // read second program
            System.out.println("Time "+Time);
            System.out.println("second Program is added");
            System.out.println("---------------------------------------");
            addNewProgram(secondProgramPath);
        }
        else if (Time == 4) {
            // read third program
            System.out.println("Time "+Time);
            System.out.println("third Program is added");
            System.out.println("---------------------------------------");
            addNewProgram(thirdProgramPath);
        }
    }

    public static void printFromTo(int from, int to){
        for(int i = from; i <= to; i++){
            System.out.println(i);
        }
    }

    public static String readFile(String path) throws FileNotFoundException {
        try {
            File file = new File(path+".txt");
            Scanner read = new Scanner(file);
            String s = "";
            while(read.hasNextLine()) {
                s = s+read.nextLine();
            }
            return s;
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return null;
    }

    public static void writeFile(String data, String path) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(path+".txt");
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        }
        catch (IOException e){
            System.out.println("File not found");
        }
    }

    public static void assign(String varName, String value,int id){
        if(memory.contains(varName)){
            System.out.println("Variable " + varName + " already exists in memory");
        }else{
            memory.add(varName, new Type(value),id);
        }
    }

    public static void print(String varName,int id){
        if(memory.contains(varName+id)){
            System.out.println(memory.get(varName,id));
        }
        else{
            System.out.println("Variable " + varName + " not found in memory");
        }
    }
    public static void semWait(String input,Process p){
        // userInput, userOutput or file
        if(input.equals("file")){
            if(src[0]!=null){
                srcQueue[0].add(p);
                p.state = States.BLOCKED;
            }
            else{
                src[0] = p;
            }
        }
        else if(input.equals("userInput")){
            if(src[1]!=null){
                srcQueue[1].add(p);
                p.state = States.BLOCKED;
            }
            else{
                src[1] = p;
            }
        }
        else{
            if(src[2]!=null){
                srcQueue[2].add(p);
                p.state = States.BLOCKED;
            }
            else{
                src[2] = p;
            }
        }
    }

    public static void semSignal(String input,Process p){
        if(input.equals("file")){
            if(src[0] != p) return;
            if(!srcQueue[0].isEmpty()){
                src[0] = srcQueue[0].poll();
                src[0].state = States.READY;
                OS.readyQueue.add(src[0]);
            }
            else{
                src[0] = null;
            }

        }
        else if(input.equals("userInput")){
            if(src[1] != p) return;
            if(!srcQueue[1].isEmpty()){
                src[1] = srcQueue[1].poll();
                src[1].state = States.READY;
                OS.readyQueue.add(src[1]);
            }
            else{
                src[1] = null;
            }
        }
        else{
            if(src[2] != p) return;
            if(!srcQueue[2].isEmpty()){
                src[2] = srcQueue[2].poll();
                src[2].state = States.READY;
                OS.readyQueue.add(src[2]);
            }
            else{
                src[2] = null;
            }
        }
    }

    public static void init() { // initialize system
        memory = new Memory();
        path = "\\\\wsl$\\Ubuntu\\home\\amir\\";
        input = new Scanner(System.in);
        src = new Process[3];
        srcQueue = new Queue[3];
        for(int i=0;i<src.length;i++){
            srcQueue[i] = new LinkedList<>();
        }
        readyQueue = new LinkedList<>();
        OS os = new OS();
        os.add();
        dispatcher dis = new dispatcher();
        dis.start();
        System.out.println("Welcome to the system, please enter program name");
    }

    public static void main(String [] args) throws IOException {
        init();
    }
}
