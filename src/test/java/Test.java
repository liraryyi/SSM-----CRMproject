public class Test {

    @org.junit.Test
    public void Test01(){
        int[] array = {45,16,56,100,1,15,65,33,25,78};

        for (int i = 0; i < array.length-1; i++) {
            for (int j = 0; j <array.length-1-i; j++) {
                if (array[j+1] > array[j]){
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }


        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
