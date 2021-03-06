package oyyt.DroneDownloader.Gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


import oyyt.DroneDownloader.Data.SQLiteConnection;
import oyyt.DroneDownloader.Downloader.DownloadTask;

public class DownloadGui {
    // mainFrame
    private JFrame mainFrame = new JFrame("Welcome DroneDownloader");
	//north
	private JPanel northPanel = new JPanel();
	private Icon logoIcon = new ImageIcon("icon/logo.png");
	private JLabel logo = new JLabel( logoIcon);
    //center
	private JScrollPane centerScrollPane;
	private JTable centerTable;
	private DefaultTableModel tableModel;
	//south
	private JPanel southPanel = new JPanel();
	private JLabel clearCompleted = new JLabel ("<HTML><U>清空已完成</U></HTML>");
	private JButton newTask = new JButton("新建任务", new ImageIcon("icon/newTask.png"));
	
	
	// userInput Url
	private JFrame urlFrame = new JFrame("新建任务");
	private Box mainBox = Box.createVerticalBox();
	private Box herizontal1 = Box.createHorizontalBox();
	private JLabel addrLabel = new JLabel("下载地址");
	private JTextField addrText= new JTextField(30);
	private Box herizontal2 = Box.createHorizontalBox();
	private JLabel nameLabel = new JLabel("文件名称");
    private JTextField nameText= new JTextField(20);
    private Box herizontal3 = Box.createHorizontalBox();
    private JLabel dirLabel = new JLabel("文件路径");
    private JTextField dirText = new JTextField(30);
    private JButton open = new JButton("打开");
    private JPanel herizontal4 = new JPanel();
	private JButton downloadNow = new JButton("立即下载");
	private JButton cancelButton = new JButton("取消");

	// popMenu
	private JPopupMenu pop = new JPopupMenu();
	private ButtonGroup flavorGroup = new ButtonGroup();
	private JRadioButtonMenuItem metaItem = new JRadioButtonMenuItem("metaItem", true);
	private JRadioButtonMenuItem nimbusItem = new JRadioButtonMenuItem("nimbusItem");
	private JRadioButtonMenuItem windowsItem = new JRadioButtonMenuItem("windowsItem");
	private JRadioButtonMenuItem classicItem = new JRadioButtonMenuItem("classicItem");
	private JRadioButtonMenuItem motifItem = new JRadioButtonMenuItem("motifItem");

	// SaveFile
	private JFileChooser chooser = new JFileChooser();

	// downloader args
	private String url;
	private String filePath;
	private String fileName;

	public void init() {
       
		initMain();
		initInputUrl();
		initSaveFile();
		addListener();
		mainFrame.pack();
		mainFrame.setSize(640, 480);
		mainFrame.setVisible(true);
	}

	public void initMain() {
		//north	
	    logo.setText("我的下载器");
	    logo.setHorizontalTextPosition(SwingConstants.LEFT);
	    northPanel.setOpaque(false);
		northPanel.add(logo);

		//center
		
		
		//south
		southPanel.setLayout(new BorderLayout());
		clearCompleted.setFont(new Font("TimesRoman", Font.BOLD, 12));
		clearCompleted.setForeground(Color.MAGENTA);
		southPanel.add(clearCompleted, BorderLayout.WEST);
		newTask.setHorizontalTextPosition(SwingConstants.RIGHT);
		newTask.setFocusPainted(false);
		newTask.setBorderPainted(false);
		newTask.setContentAreaFilled(false);
		newTask.setFocusable(false);
		southPanel.add(newTask,BorderLayout.EAST);
		
		mainFrame.add(northPanel, BorderLayout.NORTH);
		mainFrame.add(southPanel, BorderLayout.SOUTH);
	
	}
	
	
	public void initInputUrl() {
	    mainBox.add(Box.createVerticalStrut(10));
	    herizontal1.add(addrLabel);
        herizontal1.add(Box.createHorizontalStrut(50));
	    herizontal1.add(addrText);
	    herizontal1.add(Box.createHorizontalStrut(10));
	    mainBox.add(herizontal1);
	    mainBox.add(Box.createVerticalStrut(20));
	    
	    herizontal2.add(nameLabel);
	    herizontal2.add(Box.createHorizontalStrut(50));
	    herizontal2.add(nameText);
	    herizontal2.add(Box.createHorizontalStrut(10));
	    mainBox.add(herizontal2);
	    mainBox.add(Box.createVerticalStrut(20));
	    
	    herizontal3.add(dirLabel);
	    herizontal3.add(Box.createHorizontalStrut(50));
	    herizontal3.add(dirText);
	    herizontal3.add(Box.createHorizontalStrut(30));
	    herizontal3.add(open);
	    herizontal3.add(Box.createHorizontalStrut(10));
	    mainBox.add(herizontal3);
	    mainBox.add(Box.createVerticalStrut(20));
	    
	    herizontal4.setLayout(new FlowLayout(FlowLayout.CENTER));
	    herizontal4.add(downloadNow);
	    herizontal4.add(Box.createHorizontalStrut(50));
	    herizontal4.add(cancelButton);
	    herizontal4.add(Box.createHorizontalStrut(10));
	    mainBox.add(herizontal4);
	    mainBox.setComponentPopupMenu(pop);
        urlFrame.add(mainBox);
        urlFrame.setSize(200, 30);
		// popup menu
		flavorGroup.add(metaItem);
		flavorGroup.add(nimbusItem);
		flavorGroup.add(windowsItem);
		flavorGroup.add(classicItem);
		flavorGroup.add(motifItem);
		pop.add(metaItem);
		pop.add(nimbusItem);
		pop.add(windowsItem);
		pop.add(classicItem);
		pop.add(motifItem);
	
	}

	public void initSaveFile() {
		// FileChooser
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public void addListener() {
		// mainFrame
	    newTask.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
               urlFrame.pack();
               urlFrame.setVisible(true);              
            }
	    }
	    );
	    
		//urlFrame
		urlFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				urlFrame.setVisible(false);
			}
		});
        
		AddrTextChange addrListener = new AddrTextChange();	
		addrText.getDocument().addDocumentListener(addrListener);
		
		downloadNow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                    SQLiteConnection sqlite = new SQLiteConnection(url, filePath, fileName);
                    sqlite.insertData();
                    if (centerScrollPane != null){
                        mainFrame.remove(centerScrollPane);
                    }
                    ResultSet tableData = sqlite.queryData();
                    try {
                    ResultSetMetaData tableMetaData = tableData.getMetaData();
                    Vector<String> columnNames = new Vector<String>();
                    Vector<Vector<String>> data = new Vector<>();
                    for (int i = 0; i<tableMetaData.getColumnCount();i++){
                        columnNames.add(tableMetaData.getColumnName(i+1));
                    }
                    while(tableData.next()){
                        Vector<String> v = new Vector<>();
                        for (int i =0; i<tableMetaData.getColumnCount(); i++){
                            v.add(tableData.getString(i+1));
                        }
                        data.add(v);
                    }
                    tableModel = new DefaultTableModel(data, columnNames);
                    JTable table = new JTable(tableModel);
                    centerScrollPane = new JScrollPane(table);
                    mainFrame.add(centerScrollPane);
                    mainFrame.validate();
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                    DownloadTask task = new DownloadTask(url, filePath, fileName, false);
                    task.start();     
                    urlFrame.setVisible(false);
            }
		    
		});
		
 		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = chooser.showDialog(urlFrame, "Continue");
				if (result == JFileChooser.APPROVE_OPTION) {
					String str = chooser.getSelectedFile().getAbsolutePath();
					filePath = str;
					dirText.setText(filePath);				  
				}		
				if (result == JFileChooser.CANCEL_OPTION){
					// TODO cancel
				}
			}

		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				urlFrame.setVisible(false);

			}

		});
		
		ActionListener flavorListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = e.getActionCommand();
				int index = 0;
				try {
					if (content.endsWith("metaItem"))
						index = 0;
					else if (content.endsWith("nimbusItem"))
						index = 1;
					else if (content.endsWith("windowsItem"))
						index = 2;
					else if (content.endsWith("classicItem"))
						index = 3;
					else if (content.endsWith("motifItem"))
						index = 4;
					changeFlavor(index);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};

		metaItem.addActionListener(flavorListener);
		nimbusItem.addActionListener(flavorListener);
		windowsItem.addActionListener(flavorListener);
		classicItem.addActionListener(flavorListener);
		motifItem.addActionListener(flavorListener);
	}

	private void changeFlavor(int flavor) throws Exception {
		switch (flavor) {
		case 0:
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			break;
		case 1:
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			break;
		case 2:
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			break;
		case 3:
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowClassicLookAndFeel");
			break;
		case 4:
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			break;
		}
		SwingUtilities.updateComponentTreeUI(urlFrame.getContentPane());
		SwingUtilities.updateComponentTreeUI(pop);
	}
	
	public String getUrl(){
		return this.url;
	}
	public String getfilePath(){
		return this.filePath;
	}

	class AddrTextChange implements DocumentListener{
        @Override
        public void insertUpdate(DocumentEvent e) {
            setGui();        
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setGui();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setGui();
        }
        
        public void setGui(){
            url = addrText.getText();
            fileName = url.substring(url.lastIndexOf('/')+1);
            nameText.setText(fileName);
            urlFrame.setVisible(true);
        }
  
	}
	
	// 扩展AbstractTableModel，用于将一个ResultSet包装成TableModel
	class ResultSetTableModel extends AbstractTableModel
	{
	    private ResultSet rs;
	    private ResultSetMetaData rsmd;
	    // 构造器，初始化rs和rsmd两个属性
	    public ResultSetTableModel(ResultSet aResultSet)
	    {
	        rs = aResultSet;
	        try
	        {
	            rsmd = rs.getMetaData();
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }
	    // 重写getColumnName方法，用于为该TableModel设置列名
	    public String getColumnName(int c)
	    {
	        try
	        {
	            return rsmd.getColumnName(c + 1);
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	            return "";
	        }
	    }
	    // 重写getColumnCount方法，用于设置该TableModel的列数
	    public int getColumnCount()
	    {
	        try
	        {
	            return rsmd.getColumnCount();
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	            return 0;
	        }
	    }
	    // 重写getValueAt方法，用于设置该TableModel指定单元格的值
	    public Object getValueAt(int r, int c)
	    {
	        try
	        {
	           // rs.absolute(r + 1);
	            rs.next();
	            return rs.getObject(c +1 );
	        }
	        catch(SQLException e)
	        {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    // 重写getColumnCount方法，用于设置该TableModel的行数
	    public int getRowCount()
	    {
	        try
	        {
	       //     rs.last();
	           return rs.getRow();
	        }
	        catch(SQLException e)
	        {
	            e.printStackTrace();
	            return 0;
	        }
	    }
	}
	
}


