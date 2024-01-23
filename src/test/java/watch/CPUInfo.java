package watch;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class CPUInfo {
    public static void main(String[] args) {
        OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 获取系统 CPU 使用率
        System.out.println("System CPU Load: " + osBean.getSystemCpuLoad() * 100 + "%");
        // 获取进程 CPU 使用率
        System.out.println("Process CPU Load: " + osBean.getProcessCpuLoad() * 100 + "%");
    }
}