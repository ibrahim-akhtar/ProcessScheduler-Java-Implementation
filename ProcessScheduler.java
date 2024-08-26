import java.util.*;

class ProcessScheduler
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.println("\nSelect Scheduling Algorithm:");
            System.out.println("1. Priority Scheduling");
            System.out.println("2. Shortest Job First (SJF)");
            System.out.println("3. Longest Job First (LJF)");
            System.out.println("4. Shortest Remaining Time First (SRTF)");
            System.out.println("5. Longest Remaining Time First (LRTF)");
            System.out.println("6. Round Robin (RR)");
            System.out.println("7. First Come First Serve (FCFS)");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice)
            {
                case 1:
                    priorityScheduling(sc);
                    break;
                case 2:
                    sjfScheduling(sc);
                    break;
                case 3:
                    ljfScheduling(sc);
                    break;
                case 4:
                    srtfScheduling(sc);
                    break;
                case 5:
                    lrtfScheduling(sc);
                    break;
                case 6:
                    rrScheduling(sc);
                    break;
                case 7:
                    fcfsScheduling(sc);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // Method to perfrom Priroity Scheduling
    public static void priorityScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        List<Integer> ganttChart = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter priority for process " + (i + 1) + ": ");
            process.put("priority", sc.nextInt());
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            process.put("burstTime", sc.nextInt());
            
            processesList.add(process);
        }
        
        // Gets sorted based on priority(ascending order)
        processesList.sort(Comparator.comparingInt(p -> p.get("priority")));

        int currentTime = 0;
        for (Map<String, Integer> process : processesList)
        {
            process.put("startTime", currentTime);
            
            // end time calculated by adding the burst time & the start time
            process.put("endTime", currentTime + process.get("burstTime"));
            
            currentTime = process.get("endTime");
            
            ganttChart.add(process.get("id"));
        }

        displayGanttChart(ganttChart);
        displayTimings(processesList);
    }

    // Method to perfrom SJF Scheduling
    public static void sjfScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        List<Integer> ganttChart = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            process.put("burstTime", sc.nextInt());
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            process.put("arrivalTime", sc.nextInt());
            
            processesList.add(process);
        }

        // Gets sorted based on the arrival time (ascending order)
        processesList.sort(Comparator.comparingInt(p -> p.get("arrivalTime")));

        int currentTime = 0;
        
        // initialized to manage processes that are ready to execute
        List<Map<String, Integer>> readyQueue = new ArrayList<>();
        
        // initialized to manage processes that have completed execution
        List<Map<String, Integer>> completedProcesses = new ArrayList<>();

        while (!processesList.isEmpty() || !readyQueue.isEmpty())
        {
            while (!processesList.isEmpty() && processesList.get(0).get("arrivalTime") <= currentTime)
            {
                // Processes that have arrived (arrivalTime <= currentTime) are moved from processesList to readyQueue.
                readyQueue.add(processesList.remove(0));
            }

            if (readyQueue.isEmpty())
            {
                // if no processes then incremented to move forward in time and check for new arrivals
                currentTime++;
                continue;
            }
            
            // Selecting the shortest job
            
            // gets sorted based on burst time (ascending order)
            readyQueue.sort(Comparator.comparingInt(p -> p.get("burstTime")));
            Map<String, Integer> process = readyQueue.remove(0);

            process.put("startTime", currentTime);
            process.put("endTime", currentTime + process.get("burstTime"));
            currentTime = process.get("endTime");
            process.put("waitingTime", process.get("startTime") - process.get("arrivalTime"));
            process.put("completionTime", process.get("endTime"));

            completedProcesses.add(process);
            ganttChart.add(process.get("id"));
        }

        displayGanttChart(ganttChart);
        displayTimings(completedProcesses);
    }

    // Method to perfrom LJF Scheduling
    public static void ljfScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        List<Integer> ganttChart = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            process.put("burstTime", sc.nextInt());
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            process.put("arrivalTime", sc.nextInt());
            
            processesList.add(process);
        }

        // Gets sorted based on the arrival time (ascending order)
        processesList.sort(Comparator.comparingInt(p -> p.get("arrivalTime")));

        int currentTime = 0;
        
        List<Map<String, Integer>> readyQueue = new ArrayList<>();
        List<Map<String, Integer>> completedProcesses = new ArrayList<>();

        while (!processesList.isEmpty() || !readyQueue.isEmpty())
        {
            while (!processesList.isEmpty() && processesList.get(0).get("arrivalTime") <= currentTime)
            {
                readyQueue.add(processesList.remove(0));
            }

            if (readyQueue.isEmpty())
            {
                currentTime++;
                continue;
            }
            
            // gets sorted based on burst time (descending order)
            readyQueue.sort(Comparator.comparingInt(p -> -p.get("burstTime")));
            Map<String, Integer> process = readyQueue.remove(0);

            process.put("startTime", currentTime);
            process.put("endTime", currentTime + process.get("burstTime"));
            currentTime = process.get("endTime");
            process.put("waitingTime", process.get("startTime") - process.get("arrivalTime"));
            process.put("completionTime", process.get("endTime"));

            completedProcesses.add(process);
            ganttChart.add(process.get("id"));
        }

        displayGanttChart(ganttChart);
        displayTimings(completedProcesses);
    }

    // Method to perfrom SRTF Scheduling
    public static void srtfScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        
        // to keep track of the remaining burst time for each process
        Map<Integer, Integer> remainingBurstTime = new HashMap<>();
        List<Integer> ganttChart = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            process.put("burstTime", burstTime);
            remainingBurstTime.put(i + 1, burstTime);
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            process.put("arrivalTime", sc.nextInt());
            
            processesList.add(process);
        }

        processesList.sort(Comparator.comparingInt(p -> p.get("arrivalTime")));

        int currentTime = 0;
        List<Map<String, Integer>> readyQueue = new ArrayList<>();
        List<Map<String, Integer>> completedProcesses = new ArrayList<>();

        while (!processesList.isEmpty() || !readyQueue.isEmpty())
        {
            while (!processesList.isEmpty() && processesList.get(0).get("arrivalTime") <= currentTime)
            {
                readyQueue.add(processesList.remove(0));
            }

            if (readyQueue.isEmpty())
            {
                currentTime++;
                continue;
            }

            // Gets sorted based on the remaining burst time (ascending order)
            readyQueue.sort(Comparator.comparingInt(p -> remainingBurstTime.get(p.get("id"))));
            Map<String, Integer> process = readyQueue.get(0);

            // If the process is starting its execution for the first time, its startTime is set to currentTime
            if (process.get("startTime") == null)
            {
                process.put("startTime", currentTime);
            }

            remainingBurstTime.put(process.get("id"), remainingBurstTime.get(process.get("id")) - 1);
            currentTime++;
            ganttChart.add(process.get("id"));

            // If the remaining burst time becomes 0, it means the process is completely executed
            if (remainingBurstTime.get(process.get("id")) == 0)
            {
                process.put("endTime", currentTime);
                process.put("waitingTime", process.get("startTime") - process.get("arrivalTime"));
                process.put("completionTime", process.get("endTime"));
                completedProcesses.add(readyQueue.remove(0));
            }
        }

        displayGanttChart(ganttChart);
        displayTimings(completedProcesses);
    }

    // Method to perfrom LRTF Scheduling
    public static void lrtfScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        Map<Integer, Integer> remainingBurstTime = new HashMap<>();
        List<Integer> ganttChart = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            process.put("burstTime", burstTime);
            remainingBurstTime.put(i + 1, burstTime);
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            process.put("arrivalTime", sc.nextInt());
            
            processesList.add(process);
        }

        processesList.sort(Comparator.comparingInt(p -> p.get("arrivalTime")));

        int currentTime = 0;
        List<Map<String, Integer>> readyQueue = new ArrayList<>();
        List<Map<String, Integer>> completedProcesses = new ArrayList<>();

        while (!processesList.isEmpty() || !readyQueue.isEmpty())
        {
            while (!processesList.isEmpty() && processesList.get(0).get("arrivalTime") <= currentTime)
            {
                readyQueue.add(processesList.remove(0));
            }

            if (readyQueue.isEmpty())
            {
                currentTime++;
                continue;
            }

            // Gets sorted based on the remaining burst time (descending order)
            readyQueue.sort(Comparator.comparingInt(p -> -remainingBurstTime.get(p.get("id"))));
            Map<String, Integer> process = readyQueue.get(0);

            if (process.get("startTime") == null)
            {
                process.put("startTime", currentTime);
            }

            remainingBurstTime.put(process.get("id"), remainingBurstTime.get(process.get("id")) - 1);
            currentTime++;
            ganttChart.add(process.get("id"));

            if (remainingBurstTime.get(process.get("id")) == 0)
            {
                process.put("endTime", currentTime);
                process.put("waitingTime", process.get("startTime") - process.get("arrivalTime"));
                process.put("completionTime", process.get("endTime"));
                
                completedProcesses.add(readyQueue.remove(0));
            }
        }

        displayGanttChart(ganttChart);
        displayTimings(completedProcesses);
    }

    // Method to perform Round Robin Scheduling
    public static void rrScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();
        
        // To store the Time Quantum
        System.out.print("Enter time quantum: ");
        int timeQuantum = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        Map<Integer, Integer> remainingBurstTime = new HashMap<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            process.put("burstTime", burstTime);
            remainingBurstTime.put(i + 1, burstTime);
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            process.put("arrivalTime", sc.nextInt());
            
            processesList.add(process);
        }

        processesList.sort(Comparator.comparingInt(p -> p.get("arrivalTime")));

        int currentTime = 0;
        Queue<Map<String, Integer>> readyQueue = new LinkedList<>();
        List<Map<String, Integer>> completedProcesses = new ArrayList<>();
        List<Integer> ganttChart = new ArrayList<>();

        while (!processesList.isEmpty() || !readyQueue.isEmpty())
        {
            while (!processesList.isEmpty() && processesList.get(0).get("arrivalTime") <= currentTime)
            {
                readyQueue.add(processesList.remove(0));
            }

            if (readyQueue.isEmpty())
            {
                currentTime++;
                continue;
            }

            // process at the top of the readyQueue is removed
            Map<String, Integer> process = readyQueue.poll();

            // If the process is starting its execution for the first time, its startTime is set to currentTime
            if (process.get("startTime") == null)
            {
                process.put("startTime", currentTime);
            }

            // timeSlice = minimum of the time quantum and the remaining burst time of the process
            int timeSlice = Math.min(timeQuantum, remainingBurstTime.get(process.get("id")));
            
            // remaining burst time, decremented by timeSlice
            remainingBurstTime.put(process.get("id"), remainingBurstTime.get(process.get("id")) - timeSlice);
            
            currentTime += timeSlice;
            
            // process ID is added to the ganttChart for each unit of time it is being executed
            for (int i = 0; i < timeSlice; i++)
            {
                ganttChart.add(process.get("id"));
            }

            if (remainingBurstTime.get(process.get("id")) == 0)
            {
                process.put("endTime", currentTime);
                process.put("waitingTime", process.get("startTime") - process.get("arrivalTime"));
                process.put("completionTime", process.get("endTime"));
                completedProcesses.add(process);
            }
            else
            {
                while (!processesList.isEmpty() && processesList.get(0).get("arrivalTime") <= currentTime)
                {
                    readyQueue.add(processesList.remove(0));
                }
                
                // If the process is not completed, it is added back to the readyQueue to wait for its next turn
                readyQueue.add(process);
            }
        }

        displayGanttChart(ganttChart);
        displayTimings(completedProcesses);
    }

    // Mthod to perform the FCFS Scheduling
    public static void fcfsScheduling(Scanner sc)
    {
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Map<String, Integer>> processesList = new ArrayList<>();
        List<Integer> ganttChart = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> process = new HashMap<>();
            process.put("id", i + 1);
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            process.put("burstTime", sc.nextInt());
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            process.put("arrivalTime", sc.nextInt());
            
            processesList.add(process);
        }

        processesList.sort(Comparator.comparingInt(p -> p.get("arrivalTime")));

        int currentTime = 0;
        for (Map<String, Integer> process : processesList)
        {
            process.put("startTime", currentTime);
            process.put("endTime", currentTime + process.get("burstTime"));
            currentTime = process.get("endTime");
            process.put("waitingTime", process.get("startTime") - process.get("arrivalTime"));
            process.put("completionTime", process.get("endTime"));
            ganttChart.add(process.get("id"));
        }

        displayGanttChart(ganttChart);
        displayTimings(processesList);
    }

    // Method to display the Gantt Chart
    public static void displayGanttChart(List<Integer> ganttChart)
    {
        System.out.println("\nGantt Chart:");
        for (Integer processId : ganttChart)
        {
            System.out.print("| P" + processId + " ");
        }
        System.out.println("|");
    }
    
    // Method to display the different timings
    public static void displayTimings(List<Map<String, Integer>> processesList)
    {
        System.out.println("\nProcess Details:");
        System.out.println("ID\tArrival\tBurst\tStart\tEnd\tWaiting\tCompletion");
        for (Map<String, Integer> process : processesList)
        {
            System.out.println(process.get("id") + "\t" + process.get("arrivalTime") + "\t" + process.get("burstTime") + "\t" + process.get("startTime") + "\t" + process.get("endTime") + "\t" + process.get("waitingTime") + "\t" + process.get("completionTime"));
        }
    }
}