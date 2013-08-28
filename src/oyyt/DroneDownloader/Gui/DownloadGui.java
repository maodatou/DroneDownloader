package oyyt.DroneDownloader.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import oyyt.DroneDownloader.Downloader.DownloadTask;
import oyyt.DroneDownloader.Downloader.State;
import oyyt.DroneDownloader.Utility.Utility;

public class DownloadGui {
	private JFrame mainFrame = new JFrame("Welcome DroneDownloader");

	// mainFrame
	private JFrame urlFrame = new JFrame("DroneDownloader");
	private JPanel top = new JPanel();
	private JPanel left = new JPanel();
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
//	private JList<String> list = new JList<String>(listModel);
	private Icon textIcon = new ImageIcon("ico/textIcon.png");
	//top
	private Icon logoIcon = new ImageIcon("ico/logo.gif");
	private JLabel logo = new JLabel(logoIcon);
	private JLabel myDownloadLabel = new JLabel("我的下载");
	private JComboBox<String> clearAll;
	// left Tree
	private JTree tree;
	private DefaultMutableTreeNode myDownload;
	private DefaultMutableTreeNode allDownload;
	private DefaultMutableTreeNode downing;
	private DefaultMutableTreeNode completedTask;
	private DefaultMutableTreeNode music;
	private DefaultMutableTreeNode soft;
	private DefaultMutableTreeNode dustbin;
    //right
	private JPanel rightPanel = new JPanel();
	private JTable rightTable;
	// userInput Url
	private Box herizontal = Box.createHorizontalBox();
	private JButton continueButton = new JButton("继续");
	private JButton cancelButton = new JButton("取消");
	private JTextArea urlText = new JTextArea(15, 45);
	// menuBar
	private MenuBar menu = new MenuBar();
	private Menu file = new Menu("File");
	private Menu edit = new Menu("Edit");
	private Menu close = new Menu("Close");
	private MenuItem newFile = new MenuItem("New");
	private MenuItem closeAll = new MenuItem("CloseAll");
	private MenuItem closeThis = new MenuItem("Close this");
	private MenuItem copy = new MenuItem("Copy");
	private MenuItem paste = new MenuItem("Paste");
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
		mainFrame.setVisible(true);
	}

	public void initMain() {
		//top	
		Vector<String> topComBoBox = new Vector();
		topComBoBox.add("新建下载");
		topComBoBox.add("全部清空");	
		clearAll = new JComboBox<String> (topComBoBox);
		clearAll.setSelectedIndex(1);
		clearAll.setBackground(Color.white);
		myDownloadLabel.setBackground(Color.magenta);
		top.setBackground(Color.WHITE);
		top.add(logo);
		top.add(myDownloadLabel);
		top.add(clearAll);
		//left
		myDownload = new DefaultMutableTreeNode("我的下载") ;
		allDownload = new DefaultMutableTreeNode("全部任务") ;
		downing = new DefaultMutableTreeNode("正在下载") ;
		completedTask = new DefaultMutableTreeNode("已完成") ;
		music = new DefaultMutableTreeNode("音乐") ;
		soft = new DefaultMutableTreeNode("软件") ;
		dustbin = new DefaultMutableTreeNode("垃圾箱") ;
		myDownload.add(allDownload);
		myDownload.add(downing);
		myDownload.add(completedTask);
		myDownload.add(dustbin);
		completedTask.add(music);
		completedTask.add(soft);
		tree = new JTree(myDownload);
		tree.putClientProperty("JTree.lineStyle", "None");
//		list.setSelectionBackground(Color.lightGray);
//		listModel.addElement("全部任务");
//		listModel.addElement("正在下载");
//		listModel.addElement("已完成");
//		left.add(list);
		left.setBackground(Color.white);
		left.add(tree);
		//right
		Object[][] tableData =
			{
				new Object[]{"NO","rar","web.rar","Unknow","0%","UnKnow","Unknow","hot"}
			};
		Object[] columnTitle = {"状态","类型","文件名","大小","进度","剩余时间","速度","热度"};
		rightTable = new JTable(tableData,columnTitle);
		rightPanel.add(new JScrollPane(rightTable));	
		JSplitPane ud = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, rightPanel);
		JSplitPane lr = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, ud);
		mainFrame.add(lr);
	}

	public void initInputUrl() {
		herizontal.add(continueButton);
		Box.createHorizontalStrut(50);
		herizontal.add(cancelButton);

		// menuBar
		menu.add(file);
		menu.add(edit);
		file.add(newFile);
		file.addSeparator();
		file.add(close);
		close.add(closeAll);
		close.add(closeThis);
		edit.add(copy);
		edit.add(paste);

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
		urlText.setComponentPopupMenu(pop);
		urlFrame.add(urlText);
		urlFrame.add(herizontal, BorderLayout.SOUTH);
		urlFrame.setMenuBar(menu);
	}

	public void initSaveFile() {
		// FileChooser
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public void addListener() {
		// mainFrame
		clearAll.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				int item = clearAll.getSelectedIndex();
				//新建下载
				if (item == 0){
					urlFrame.pack();
					urlFrame.setVisible(true);
				}
				//全部清空
				if (item == 1){
					rightTable.setVisible(false);
				}
				
			}
			
		});
		// //urlFrame
		urlFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				urlFrame.setVisible(false);
			}
		});

		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = chooser.showDialog(urlFrame, "确定");
				url = urlText.getText();
				if (result == JFileChooser.APPROVE_OPTION) {
					String str = chooser.getSelectedFile().getAbsolutePath();
					filePath = str;
					urlFrame.setVisible(false);
					 DownloadTask task = new DownloadTask(url, filePath, fileName, false);
				     task.start();
				     
				     /*
				        do {
				            State state = task.getState();
				            if (state == State.FINISH) {
				                System.out.println("\nFINISH");
				                break;
				            }

				            if (state == State.ERROR) {
				                System.out.println("\nError:\n\t" + task.getErrorMsg());
				                break;
				            }

				            if (state == State.DOWNLOADING) {
				                System.out.print("\r" + task.getFileName() + ": ");
				                System.out.print("\t" + Utility.formatSpeed(task.getSpeed()));
				                System.out.print(String.format("\t%3d%%", task.getPercentage()));
				            }

				            try {
				                Thread.sleep(1000);
				            } catch (InterruptedException e1) {
				                System.err.println("Thread is interrupted.");
				            }
				        }
				        while(true);
				        */ 
				}
			
				if (result == JFileChooser.CANCEL_OPTION){
					// TODO cancel
				}
				// just test
				// urlText.append(filePath + "    ");
				// urlText.append(url);
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

}
