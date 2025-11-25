package algorithms;

import java.util.*;
import model.SortingAlgorithm;
import model.Step;

public class AlgorithmDefinitions {

    public static class BubbleSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Bubble Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "for i from 0 to n-1 do", "  for j from 0 to n-i-2 do", "    if arr[j] > arr[j+1] then", "      swap arr[j] and arr[j+1]" };}
        @Override public String getDefinition(){ return "Bubble Sort repeatedly compares adjacent elements and swaps them if out of order."; }
        @Override public String getAvgTime(){ return "O(n²)"; }
        @Override public String getWorstTime(){ return "O(n²)"; }
        @Override public String getSpace(){ return "O(1)"; }
        @Override public String getTypicalUse(){ return "Teaching, tiny arrays"; }
        @Override public String getAdvantages(){ return "Simple to implement; stable; minimal memory"; }
        @Override public String getDisadvantages(){ return "Very slow on large lists; O(n²) time"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> steps = new ArrayList<>();
            int[] arr = input.clone(); int n = arr.length;
            steps.add(new Step(Step.Type.SET,-1,-1,arr,"Initial array",-1));
            for (int i=0;i<n;i++){
                for (int j=0;j<n-i-1;j++){
                    steps.add(new Step(Step.Type.COMPARE,j,j+1,arr,"Compare index "+j+" and "+(j+1),1));
                    if (arr[j] > arr[j+1]){
                        int t=arr[j]; arr[j]=arr[j+1]; arr[j+1]=t;
                        steps.add(new Step(Step.Type.SWAP,j,j+1,arr,"Swap positions "+j+" and "+(j+1),2));
                    }
                }
                steps.add(new Step(Step.Type.MARK_FINAL,n-1-i,-1,arr,"Mark final index "+(n-1-i),0));
            }
            return steps;
        }
    }

    public static class InsertionSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Insertion Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "for i from 1 to n-1 do", "  key = arr[i]", "  j = i-1", "  while j>=0 and arr[j] > key do", "    arr[j+1] = arr[j]; j = j-1", "  arr[j+1] = key" };}
        @Override public String getDefinition(){ return "Insertion Sort builds the sorted array one element at a time by inserting elements."; }
        @Override public String getAvgTime(){ return "O(n²)"; }
        @Override public String getWorstTime(){ return "O(n²)"; }
        @Override public String getSpace(){ return "O(1)"; }
        @Override public String getTypicalUse(){ return "Small or mostly sorted arrays"; }
        @Override public String getAdvantages(){ return "Efficient for small or nearly-sorted arrays; stable"; }
        @Override public String getDisadvantages(){ return "Poor performance on large random arrays"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> steps = new ArrayList<>();
            int[] arr = input.clone(); steps.add(new Step(Step.Type.SET,-1,-1,arr,"Initial array",-1));
            int n = arr.length;
            for (int i=1;i<n;i++){
                int key = arr[i];
                steps.add(new Step(Step.Type.COMPARE,i,-1,arr,"Pick key at index "+i,0));
                int j = i-1;
                while (j>=0 && arr[j] > key){
                    steps.add(new Step(Step.Type.COMPARE,j,i,arr,"Compare arr["+j+"] and key",3));
                    arr[j+1] = arr[j];
                    steps.add(new Step(Step.Type.SET,j+1,-1,arr,"Shift to index "+(j+1),4));
                    j--;
                }
                arr[j+1] = key;
                steps.add(new Step(Step.Type.SET,j+1,-1,arr,"Insert key at "+(j+1),5));
            }
            return steps;
        }
    }

    // For brevity, I'll include the rest (Selection, Merge, Quick, Heap, Shell, Counting) 
    // following the same pattern. You can paste the previous logic here.
    // Assume Selection, Merge, Quick, Heap, Shell, Counting classes are here...
    
    public static class SelectionSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Selection Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "for i from 0 to n-2 do", "  minIndex = i", "  for j from i+1 to n-1 do", "    if arr[j] < arr[minIndex] then minIndex = j", "  swap arr[i] and arr[minIndex]" };}
        @Override public String getDefinition(){ return "Selection Sort repeatedly finds the minimum element."; }
        @Override public String getAvgTime(){ return "O(n²)"; }
        @Override public String getWorstTime(){ return "O(n²)"; }
        @Override public String getSpace(){ return "O(1)"; }
        @Override public String getTypicalUse(){ return "When swaps are expensive"; }
        @Override public String getAdvantages(){ return "Performs minimal swaps"; }
        @Override public String getDisadvantages(){ return "Still O(n²) comparisons; not stable"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> steps = new ArrayList<>();
            int[] arr = input.clone(); int n=arr.length;
            steps.add(new Step(Step.Type.SET,-1,-1,arr,"Initial array",-1));
            for (int i=0;i<n-1;i++){
                int min=i; steps.add(new Step(Step.Type.MARK_FINAL,i,-1,arr,"Consider index "+i,1));
                for (int j=i+1;j<n;j++){
                    steps.add(new Step(Step.Type.COMPARE,j,min,arr,"Compare "+arr[j]+" and "+arr[min],2));
                    if (arr[j] < arr[min]){ min=j; steps.add(new Step(Step.Type.SET,min,-1,arr,"New minIndex="+min,3)); }
                }
                if (min != i){ int t=arr[i]; arr[i]=arr[min]; arr[min]=t; steps.add(new Step(Step.Type.SWAP,i,min,arr,"Swap i and min",4)); }
                steps.add(new Step(Step.Type.MARK_FINAL,i,-1,arr,"Mark final "+i,0));
            }
            if (n>0) steps.add(new Step(Step.Type.MARK_FINAL,n-1,-1,arr,"Last final",0));
            return steps;
        }
    }

    public static class MergeSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Merge Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "mergeSort(arr, l, r):", "  if l < r then", "    m = (l+r)/2", "    mergeSort(arr, l, m)", "    mergeSort(arr, m+1, r)", "    merge(arr, l, m, r)" };}
        @Override public String getDefinition(){ return "Merge Sort splits array, sorts halves, and merges sorted halves."; }
        @Override public String getAvgTime(){ return "O(n log n)"; }
        @Override public String getWorstTime(){ return "O(n log n)"; }
        @Override public String getSpace(){ return "O(n)"; }
        @Override public String getTypicalUse(){ return "Large datasets"; }
        @Override public String getAdvantages(){ return "Stable and predictable"; }
        @Override public String getDisadvantages(){ return "Needs extra space"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> steps = new ArrayList<>();
            int[] arr = input.clone(); steps.add(new Step(Step.Type.SET,-1,-1,arr,"Initial array",-1));
            mergesort(arr,0,arr.length-1,steps);
            return steps;
        }
        private void mergesort(int[] a,int l,int r, List<Step> s){
            if (l>=r) return;
            int m=(l+r)/2; mergesort(a,l,m,s); mergesort(a,m+1,r,s);
            int n1=m-l+1, n2=r-m; int[] L=new int[n1], R=new int[n2];
            for(int i=0;i<n1;i++) L[i]=a[l+i]; for(int j=0;j<n2;j++) R[j]=a[m+1+j];
            int i=0,j=0,k=l;
            while(i<n1 && j<n2){
                s.add(new Step(Step.Type.COMPARE,l+i,m+1+j,a,"Compare left and right",5));
                if (L[i] <= R[j]){ a[k]=L[i++]; s.add(new Step(Step.Type.SET,k,-1,a,"Write from left to "+k,5)); }
                else { a[k]=R[j++]; s.add(new Step(Step.Type.SET,k,-1,a,"Write from right to "+k,5)); }
                k++;
            }
            while(i<n1){ a[k++]=L[i++]; s.add(new Step(Step.Type.SET,k-1,-1,a,"Write remaining left",5)); }
            while(j<n2){ a[k++]=R[j++]; s.add(new Step(Step.Type.SET,k-1,-1,a,"Write remaining right",5)); }
            for (int t=l;t<=r;t++) s.add(new Step(Step.Type.MARK_FINAL,t,-1,a,"Merged ["+l+","+r+"]",0));
        }
    }

    public static class QuickSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Quick Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "quickSort(arr, low, high):", "  if low < high then", "    pi = partition(arr, low, high)", "    quickSort(arr, low, pi-1)", "    quickSort(arr, pi+1, high)" };}
        @Override public String getDefinition(){ return "Quick Sort partitions array around a pivot and recursively sorts partitions."; }
        @Override public String getAvgTime(){ return "O(n log n)"; }
        @Override public String getWorstTime(){ return "O(n²)"; }
        @Override public String getSpace(){ return "O(log n)"; }
        @Override public String getTypicalUse(){ return "General purpose sorting"; }
        @Override public String getAdvantages(){ return "Very fast on average"; }
        @Override public String getDisadvantages(){ return "Worst-case O(n²)"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> s = new ArrayList<>(); int[] arr = input.clone(); s.add(new Step(Step.Type.SET,-1,-1,arr,"Initial array",-1));
            quick(arr,0,arr.length-1,s); return s;
        }
        private void quick(int[] arr,int l,int r, List<Step> s){
            if (l<r){ int p = partition(arr,l,r,s); quick(arr,l,p-1,s); quick(arr,p+1,r,s); }
        }
        private int partition(int[] a,int l,int r, List<Step> s){
            int pivot = a[r]; s.add(new Step(Step.Type.PIVOT,r,-1,a,"Pivot="+pivot,2)); int i = l-1;
            for (int j=l;j<r;j++){
                s.add(new Step(Step.Type.COMPARE,j,r,a,"Compare "+a[j]+" and pivot",2));
                if (a[j] < pivot){ i++; int t=a[i]; a[i]=a[j]; a[j]=t; s.add(new Step(Step.Type.SWAP,i,j,a,"Swap for partition",2)); }
            }
            int t=a[i+1]; a[i+1]=a[r]; a[r]=t; s.add(new Step(Step.Type.SWAP,i+1,r,a,"Place pivot at "+(i+1),2)); return i+1;
        }
    }

    public static class HeapSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Heap Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "buildMaxHeap(arr)", "for i from n-1 down to 1 do", "  swap arr[0] and arr[i]", "  heapify(arr, 0, i)" };}
        @Override public String getDefinition(){ return "Heap Sort builds a max-heap and extracts max element."; }
        @Override public String getAvgTime(){ return "O(n log n)"; }
        @Override public String getWorstTime(){ return "O(n log n)"; }
        @Override public String getSpace(){ return "O(1)"; }
        @Override public String getTypicalUse(){ return "In-place sorting"; }
        @Override public String getAdvantages(){ return "Good worst-case time"; }
        @Override public String getDisadvantages(){ return "Not stable"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> s = new ArrayList<>(); int n=input.length; int[] a=input.clone(); s.add(new Step(Step.Type.SET,-1,-1,a,"Initial array",-1));
            for (int i=n/2-1;i>=0;i--) heapify(a,n,i,s);
            for (int i=n-1;i>0;i--){ int t=a[0]; a[0]=a[i]; a[i]=t; s.add(new Step(Step.Type.SWAP,0,i,a,"Move max to end",1)); heapify(a,i,0,s); s.add(new Step(Step.Type.MARK_FINAL,i,-1,a,"Final index "+i,0)); }
            if (n>0) s.add(new Step(Step.Type.MARK_FINAL,0,-1,a,"Final index 0",0)); return s;
        }
        private void heapify(int[] a,int n,int i, List<Step> s){
            int largest=i,l=2*i+1,r=2*i+2;
            if (l<n){ s.add(new Step(Step.Type.COMPARE,l,largest,a,"Compare left",0)); if (a[l]>a[largest]) largest=l; }
            if (r<n){ s.add(new Step(Step.Type.COMPARE,r,largest,a,"Compare right",0)); if (a[r]>a[largest]) largest=r; }
            if (largest!=i){ int t=a[i]; a[i]=a[largest]; a[largest]=t; s.add(new Step(Step.Type.SWAP,i,largest,a,"Heapify swap",0)); heapify(a,n,largest,s); }
        }
    }

    public static class ShellSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Shell Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "choose gap", "for gap > 0 do", "  do insertion sort with that gap" };}
        @Override public String getDefinition(){ return "Shell Sort improves insertion sort by using decreasing gaps."; }
        @Override public String getAvgTime(){ return "Varies"; }
        @Override public String getWorstTime(){ return "O(n²)"; }
        @Override public String getSpace(){ return "O(1)"; }
        @Override public String getTypicalUse(){ return "Improvement for insertion sort"; }
        @Override public String getAdvantages(){ return "Faster than insertion sort"; }
        @Override public String getDisadvantages(){ return "Complex gap sequence"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> s = new ArrayList<>(); int[] a = input.clone(); s.add(new Step(Step.Type.SET,-1,-1,a,"Initial array",-1));
            int n=a.length;
            for (int gap=n/2; gap>0; gap/=2){
                for (int i=gap;i<n;i++){
                    int temp=a[i]; int j; s.add(new Step(Step.Type.COMPARE,i,i-gap,a,"Start gapped insertion",1));
                    for (j=i;j>=gap && a[j-gap] > temp; j-=gap){ a[j]=a[j-gap]; s.add(new Step(Step.Type.SET,j,-1,a,"Shift gapped",1)); }
                    a[j]=temp; s.add(new Step(Step.Type.SET,j,-1,a,"Place temp",1));
                }
            }
            for (int i=0;i<n;i++) s.add(new Step(Step.Type.MARK_FINAL,i,-1,a,"Final",0)); return s;
        }
    }

    public static class CountingSortRecorder implements SortingAlgorithm {
        @Override public String getName(){ return "Counting Sort"; }
        @Override public String[] getPseudocodeLines(){ return new String[]{ "find max K", "count occurrences", "prefix sum", "place elements" };}
        @Override public String getDefinition(){ return "Counting Sort counts occurrences of integers."; }
        @Override public String getAvgTime(){ return "O(n + K)"; }
        @Override public String getWorstTime(){ return "O(n + K)"; }
        @Override public String getSpace(){ return "O(n + K)"; }
        @Override public String getTypicalUse(){ return "Small range integers"; }
        @Override public String getAdvantages(){ return "Linear time when K small"; }
        @Override public String getDisadvantages(){ return "Requires O(K) memory"; }
        @Override public List<Step> generateSteps(int[] input){
            List<Step> s = new ArrayList<>(); if (input.length==0) return s;
            int max = Arrays.stream(input).max().orElse(0);
            if (max < 0){ s.add(new Step(Step.Type.SET,-1,-1,input.clone(),"Requires non-negative ints",-1)); return s; }
            int[] a = input.clone(); s.add(new Step(Step.Type.SET,-1,-1,a,"Initial array",-1));
            int[] count = new int[max+1]; for (int v : a) count[v]++;
            int idx=0; for (int k=0;k<=max;k++){ while (count[k]-- > 0){ a[idx++] = k; s.add(new Step(Step.Type.SET,idx-1,-1,a,"Place value "+k,2)); } }
            for (int i=0;i<a.length;i++) s.add(new Step(Step.Type.MARK_FINAL,i,-1,a,"Final",0)); return s;
        }
    }
}