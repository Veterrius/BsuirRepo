import java.text.DecimalFormat;

/*public class Main {
    public static void main(String[] args) {
        double ai[]={0.15, 0.2, 0.1, 0.25, 0.3};//коэфф ai
        double qimax[]={5.0, 5.0, 45.0, 870.0, 12.0}; //лучшие параметры
        double qi[]={2.0, 5.0, 45.0, 700.0, 8.0}; //требуемые параметры
        double elem[][]=
        {
                {1.0, 5.0, 45.0, 870.0, 12.0},
                {2.0, 4.0, 65.0, 700.0, 10.0},
                {3.0, 2.0, 68.0, 420.0, 4.0},
                {4.0, 2.0, 73.0, 600.0, 8.0},
                {5.0, 2.0, 85.0, 400.0, 1.0}
        };
        double d=0;
        StringBuilder st=new StringBuilder();
        for(int i=0; i<5; i++){
            d=0;
            for (int j=0; j<5; j++){
                d+=(ai[j]/qimax[j])*Math.abs(qi[j]-elem[i][j]);
                st.append(ai[j]).append("/").append((int)qimax[j]).append("*Abs(").append((int)qi[j]).append("-").append((int)elem[i][j]).append(")").append("+");
            }
            st.replace(st.length()-1, st.length(), "");
            System.out.print(st);
              ;
            System.out.println("="+formattedDouble);
            st.replace(0, st.length(), "");
        }
    }
}*/

public class Main {
    public static void main(String[] args) {
        double ai[]={0.2, 0.2, 0.15, 0.15, 0.3};//коэфф ai
        double qimax[]={10000.0, 400.0, 5.0, 150.0, 5.0}; //лучшие параметры
        double qi[]={10000.0, 430.0, 5.0, 60.0, 5.0}; //требуемые параметры
        double elem[][]=
                {
                        {4200.0, 510.0, 4.0, 393.0, 3.0},
                        {10280.0, 586.0, 5.0, 11.0, 5.0},
                        {12000.0, 586.0, 4.0, 27.0, 5.0},
                        {1600.0, 754.0, 4.0, 3.0, 5.0},
                        {1000.0, 386.0, 3.0, 168.0, 5.0}
                };
        double d=0;
        StringBuilder st=new StringBuilder();
        for(int i=0; i<5; i++){
            d=0;
            for (int j=0; j<5; j++){
                d+=(ai[j]/qimax[j])*Math.abs(qi[j]-elem[i][j]);
                st.append(ai[j]).append("/").append((int)qimax[j]).append("*Abs(").append((int)qi[j]).append("-").append((int)elem[i][j]).append(")").append("+");
            }
            st.replace(st.length()-1, st.length(), "");
            System.out.print(st);
            String formattedDouble = new DecimalFormat("#0.00000").format(d);
            System.out.println("="+formattedDouble);
            st.replace(0, st.length(), "");
        }
    }
}
