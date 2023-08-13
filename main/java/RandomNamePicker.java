import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNamePicker extends JFrame {
    private List<String> names; // 存储名字列表

    private Random random; // 随机数生成器

    private Timer timer; // 定时器，用于定时选取名字

    private JLabel selectedNameLabel; // 显示被选中的名字的标签

    private JButton startButton; // 开始按钮

    private JButton stopButton; // 停止按钮

    private JButton uploadButton; // 上传名单按钮

    private boolean isPicking; // 是否正在选取名字

    public RandomNamePicker() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("点名小程序");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//可关闭窗口
        setSize(400, 350);//窗体大小
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));//垂直排列

        names = new ArrayList<>(); // 初始化名字列表
        random = new Random(); // 初始化随机数生成器
        isPicking = false; // 初始化选择状态

        selectedNameLabel = new JLabel(""); // 创建显示被选中名字的标签
        selectedNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        selectedNameLabel.setVisible(false); // 初始时不可见
        add(selectedNameLabel);
        add(Box.createVerticalStrut(30));

        // 创建开始按钮
        startButton = new JButton("开始");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPicking) {
                    startPicking(); // 开始选择名字
                }
            }
        });
        add(startButton);
        add(Box.createVerticalStrut(30));

        // 创建停止按钮
        stopButton = new JButton("停止");
        stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        stopButton.setEnabled(false); // 初始状态下停止按钮不可用
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPicking) {
                    stopPicking(); // 停止选择名字
                }
            }
        });
        add(stopButton);
        add(Box.createVerticalStrut(30));


        // 创建上传名单按钮
        uploadButton = new JButton("上传名单");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadNames(); // 上传名单
            }
        });
        add(uploadButton);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * 开始选择名字
     */
    private void startPicking() {
        if (names.size() > 0) { // 如果名字列表不为空
            timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pickRandomName(); // 随机选取名字
                }
            });
            timer.start(); // 启动定时器
            isPicking = true; // 更新选择状态
            startButton.setEnabled(false); // 开始按钮不可用
            stopButton.setEnabled(true); // 停止按钮可用
            selectedNameLabel.setVisible(false); // 隐藏被选中的名字标签
        } else {
            JOptionPane.showMessageDialog(this, "名单为空，请上传名单！"); // 显示提示信息
        }
    }

    /**
     * 停止选择名字
     */
    private void stopPicking() {
        if (timer != null) {
            timer.stop(); // 停止定时器
        }
        isPicking = false; // 更新选择状态
        startButton.setEnabled(true); // 开始按钮可用
        stopButton.setEnabled(false); // 停止按钮不可用
        int index = random.nextInt(names.size()); // 随机选取一个名字
        JOptionPane.showMessageDialog(this, "选到的名字是：" + names.get(index)); // 显示选中的名字
        selectedNameLabel.setText(""); // 清空被选中的名字标签
        selectedNameLabel.setVisible(false); // 隐藏被选中的名字标签
    }

    /**
     * 随机选取名字
     */
    private void pickRandomName() {
        int index = random.nextInt(names.size()); // 随机选取一个名字
        selectedNameLabel.setText("当前被选中的名字：" + names.get(index)); // 更新被选中的名字标签
    }

    /**
     * 从文件中上传名字列表
     */
    private void uploadNames() {
        JFileChooser fileChooser = new JFileChooser(); // 创建文件选择对话框
        int result = fileChooser.showOpenDialog(this); // 显示文件选择对话框
        if (result == JFileChooser.APPROVE_OPTION) { // 如果选择了文件
            File selectedFile = fileChooser.getSelectedFile(); // 获取选择的文件
            try {
                names = loadNamesFromFile(selectedFile); // 从文件中加载名字列表
                JOptionPane.showMessageDialog(this, "名单上传成功！"); // 显示提示信息
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "读取名单文件出错！"); // 显示错误信息
            }
        }
    }

    /**
     * 从文件中加载名字列表
     */
    private List<String> loadNamesFromFile(File file) throws IOException {
        List<String> names = new ArrayList<>(); // 创建一个空的名字列表
        if (file.exists() && file.isFile()) { // 如果文件存在且是一个普通文件
            BufferedReader reader = new BufferedReader(new FileReader(file)); // 创建文件读取器
            String line;
            while ((line = reader.readLine()) != null) { // 逐行读取文件内容
                names.add(line.trim()); // 添加名字到列表，去除首尾空格
            }
            reader.close(); // 关闭文件读取器
        }
        return names; // 返回加载的名字列表
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandomNamePicker::new);
    }
}
