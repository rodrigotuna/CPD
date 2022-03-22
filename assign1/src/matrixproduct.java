import java.util.*;  

class MatrixProduct {
    public static void main(String args[]){
        char c;
        int lin, col, blockSize;
        int op;

        MatrixProduct mt = new MatrixProduct();

        long values[] = new long[2];
        int ret;

        op=1;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("\n1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.print("Selection?: ");
            op = sc.nextInt();

            if(op == 0) break;

            System.out.print("Dimensions: lins=cols ? ");
            lin = sc.nextInt();
            col = lin;

            switch(op){
                case 1:
                    mt.OnMult(lin,col);
                    break;
                case 2:
                    mt.OnMultLine(lin, col); 
                    break;
            }

        } while(op!=0);
        
       sc.close();
    }

    private void OnMult(int m_ar, int m_br){
        double temp;
        int i, j, k;

        double pha[] = new double[m_ar * m_ar];
        double phb[] = new double[m_ar * m_ar];
        double phc[] = new double[m_ar * m_ar];

        for(i=0; i<m_ar; i++)
		    for(j=0; j<m_ar; j++)
			    pha[i*m_ar + j] = (double)1.0;



        for(i=0; i<m_br; i++)
            for(j=0; j<m_br; j++)
                phb[i*m_br + j] = (double)(i+1);

        long time1 = System.nanoTime();

        for(i=0; i<m_ar; i++)
        {	for( j=0; j<m_br; j++)
            {	temp = 0;
                for( k=0; k<m_ar; k++)
                {	
                    temp += pha[i*m_ar+k] * phb[k*m_br+j];
                }
                phc[i*m_ar+j]=temp;
            }
        }

        long time2 = System.nanoTime();
        double timeElapsed = (double)(time2 - time1)/ 1_000_000_000;
        System.out.printf("Time: %f seconds\n", timeElapsed);

        System.out.println("Result matrix: ");
        for(i=0; i<1; i++)
        {	for(j=0; j<Math.min(10,m_br); j++)
            {
                System.out.print(phc[j]);
                System.out.print(" ");
            }
        }
        System.out.println("");
    }

    private void OnMultLine(int m_ar, int m_br){
        double temp;
        int i, j, k;

        double pha[] = new double[m_ar * m_ar];
        double phb[] = new double[m_ar * m_ar];
        double phc[] = new double[m_ar * m_ar];

        for(i=0; i<m_ar; i++)
		    for(j=0; j<m_ar; j++)
			    pha[i*m_ar + j] = (double)1.0;

        for(i=0; i<m_br; i++)
            for(j=0; j<m_br; j++)
                phb[i*m_br + j] = (double)(i+1);
        
        for(i=0; i<m_ar; i++)
		    for(j=0; j<m_ar; j++)
			    phc[i*m_ar + j] = (double)0.0;

        long time1 = System.nanoTime();

        for(i=0; i<m_ar; i++)
        {	for( k=0; k<m_ar; k++)
            {
                for( j=0; j<m_br; j++)
                {	
                    phc[i*m_ar+j] += pha[i*m_ar+k] * phb[k*m_br+j];
                }
            }
        }

        long time2 = System.nanoTime();
        double timeElapsed = (double)(time2 - time1)/ 1_000_000_000;
        System.out.printf("Time: %f seconds\n", timeElapsed);

        System.out.println("Result matrix: ");
        for(i=0; i<1; i++)
        {	for(j=0; j<Math.min(10,m_br); j++)
            {
                System.out.print(phc[j]);
                System.out.print(" ");
            }
        }
        System.out.println("");
    }
}