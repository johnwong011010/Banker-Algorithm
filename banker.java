import java.util.Scanner;

import javax.swing.plaf.TextUI;
public class banker{
    int resource = 4; // how many the resource is
    int p = 5; //number of process
    int[][] max;
    int[][] allocation;
    int[][] need = new int[p][resource];
    int[] avali_resource;
    int[] safe_seqence = new int[p];
    void initializeValues(){
        allocation = new int[][] {{3,1,4,1},{2,1,0,2},{2,4,1,3},{4,1,1,0},{2,2,2,1}};//process allocation resource
        max = new int[][] {{6,4,7,3},{4,2,3,2},{2,5,3,3},{6,3,3,2},{5,6,7,5}};//max resource request
        avali_resource = new int[] {2,2,2,4}; //available system resource
        System.out.print("The available resource:"); //print the available system resource
        for(int i=0;i<avali_resource.length;i++){
            System.out.print(" "+avali_resource[i]+" ");
        }
        System.out.println();

        System.out.println("Both process allocation and max require resource:");
        for (int i=0;i<p;i++){
            int pid = i+1;
            System.out.print("Process"+" "+pid+":");
            for (int j=0;j<resource;j++){
                System.out.print(" "+allocation[i][j]+" ");
            }
            System.out.print("|");
            for (int k=0;k<resource;k++){
                System.out.print(" "+max[i][k]+" ");
            }
            System.out.println();
        }
    }
    void check_ifSafe(){
        int count = 0;
        boolean[] visited = new boolean[p]; //save the process done or not
        for (int i=0;i<visited.length;i++){
            visited[i] = false; //set all process is not done
        }

        int[] work = new int[resource]; //store the copy of available resource number
        for (int i=0;i<work.length;i++){
            work[i] = avali_resource[i];
        }

        while (count<p){ //try to find the safe seqence
            boolean flag = false;
            for(int i=0;i<p;i++){ //check every process
                if (visited[i] == false){
                    int j;
                    //check the resource enough or not
                    for (j=0;j<resource;j++){ 
                        if (need[i][j]>work[j]){
                            break;
                        }
                    }
                    if (j==resource){ //this process can finish it,mark this process is finished
                        safe_seqence[count++] = i+1;
                        visited[i] = true;
                        flag = true;
                        for (j = 0;j<resource;j++){ //release system resource which this process hold
                            work[j] = work[j] + allocation[i][j];
                        }
                    }
                }
            }
            if (flag == false){
                break;
            }
        }
        //print the result
        if (count < p){
            System.out.println("Can not find the safe seqence!");
        }
        else{
            System.out.println("The safe seqence is:");
            for(int i=0;i<safe_seqence.length;i++){
                System.out.print("P"+safe_seqence[i]);
                if (i != p-1){
                    System.out.print("->");
                }   
            }
        }
    }
    void calculateNeed(){
        for (int i=0;i<p;i++){
            for (int j=0;j<resource;j++){
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
        System.out.print("\n"+"Current system remain resource:");
        for (int r=0;r<avali_resource.length;r++){
            System.out.print(" "+avali_resource[r]+" ");
        }
        System.out.println();
        System.out.println("The resource that process need:");
        for (int i=0;i<p;i++){
            int pid = i+1;
            System.out.print("Process "+pid+" :");
            for (int j=0;j<resource;j++){
                System.out.print(" "+need[i][j]+" ");
            }
            System.out.println();
        }
    }
    void check(int pid,int[] RR){
        boolean safe = false;
        if (pid>=p || pid<0){
            System.out.print("This process id is not exist!!");
            return;
        }
        else{
            for (int i=0;i<resource;i++){
                if (RR[i] > need[pid][i] || RR[i] > avali_resource[i]){
                    System.out.print("This request can not be accept!!");
                    return;
                }
            }
            safe = true;
            if (safe == true){
                for (int j=0;j<resource;j++){
                    allocation[pid][j] += RR[j];
                    avali_resource[j] -= RR[j];
                }
                
            }
        }
        calculateNeed();
        check_ifSafe();
    }
    public static void main(String[] argv){
        int resource = 4;
        banker test1 = new banker();
        test1.initializeValues();;
        test1.calculateNeed();
        test1.check_ifSafe();
        //boolean
        Scanner s = new Scanner(System.in);
        System.out.println("\n"+"Do you want to continue Banker's Algorithm?If you want to continue,please enter yes.");
        String check = s.next();
        while (check.equals("yes")){
            System.out.print("\n"+"Please enter process number:");
            int pid = s.nextInt()-1;
            int[] RequestResource = new int[resource];
            for (int i=0;i<resource;i++){
                System.out.print("Please enter Resource"+i+":");
                RequestResource[i] = s.nextInt();
            }
            test1.check(pid,RequestResource);
            System.out.println("\n"+"Do you want to continue? If you do,enter yes");
            String confirm = s.next();
            if (!confirm.equals("yes")){
                check = "No";
            }
        }
    }
}