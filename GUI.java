import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Comparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.IllegalStateException;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lib.RecordSystem;
import lib.Food;
import lib.ImageFilter;

public class GUI extends JFrame {
    private static final String DESTINATION_DIR = "icon/";
    private static final String RECORDS_DIR = "record";
    private static final String RECORDS_FILE = "record/records.txt";
    private static final String PHOTOLIST_DIR = "record";
    private static final String PHOTOLIST_FILE = "record/photoList.txt";
    private static final int ITEM_ROW = 3; // 顯示的 row 數量
    private static final int ITEM_COLUMN = 4;// 顯示的 column 數量
    private Set<Character> zhuyinSymbols;
    private JLabel topLabel;
    private JPanel topPanel;//頂部:顯示方式、排序方式
    private JLabel itemLabel;
    private JLabel[] itemFields;
    private Icon[] icons;
    private JScrollPane scrollPane;//滾動條
    private JScrollPane iconScrollPane;//照片滾動條
    private JComponent itemViewer;
    private JPanel viewPanel;
    private JPanel itemPanel;//儲存物品的地方
    private JPanel buttonPanel;//存放按鈕的地方
    private JTable itemTable;//儲存物品的表格
    private String[] tableColumnNames = {"名稱", "類型", "有效日期"};
    private JLabel createLabel;
    private JPanel createPanel;//創建物品
    private JTextField newItemName;
    private JTextField newItemType;
    private JTextField newItemDate;
    private JList<String> newItemIcon;
    private JLabel bottomLabel;
    private JPanel bottomPanel;//底部:日期、設置、新增
    private JPanel photoPanel;//顯示照片處理相關的按鈕
    private JButton showByButton;//顯示方式切換



    private JButton sortByButton;//排序方式
    private JButton createItemButton;//按鈕:新增物品
    private JButton deleteItemButton;//按鈕:刪除物品
    private JButton uploadPhotoButton;//按鈕:上傳照片
    private JButton deletePhotoButton;//按鈕:刪除照片
    private JTextField timeField;//現在日期
    private RecordSystem recordSystem;//系統


    private String displayMethod = "圖片";//顯示方式
    private String sortMethod = "類型";//排序方式
    private int item_counts;
    ArrayList<Food> foodList;

    private ArrayList<String> photoList;
    private Food[] allFood;
    private String[][] foodData;

    private JPanel outerPanel;

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

//-------------------------------------------------search---------------------------------------------------------------
    private SearchSystem searchSystem;
    ArrayList<Food> searchFoodList;
    private JTextField TestText;
    private boolean flag = false;
    private boolean nameSearchFlag = false;
    private boolean dateSearchFlag = false;
    private String[][] searchFoodData;

    private JPanel secondPanel;
    private JComboBox<Integer> fromYearComboBox;
    private JComboBox<Integer> fromMonthComboBox;
    private JComboBox<Integer> fromDayComboBox;
    private JComboBox<Integer> toYearComboBox;
    private JComboBox<Integer> toMonthComboBox;
    private JComboBox<Integer> toDayComboBox;
    private JLabel nameSearchLabel;
    private JLabel dateSearchLabel;
    private JLabel toLabel;
    private JButton searchButton;
    private JButton searchButtonForNameSearch;
    private JButton clearButton;
    //------------------------------------------------------------------------------------------------------------------

    // Constructor
    public GUI() {
        super("冰箱記帳");
        outerPanel = new JPanel();
        recordSystem = new RecordSystem();
        item_counts = 0;
        photoList = new ArrayList<String>();
        foodData = new String[0][];
        Date today = new Date();




        outerPanel.setLayout(new GridLayout(8, 1));
        Handler handler = new Handler();
        //------top------
        topPanel = new JPanel();
        showByButton = new JButton("以" + "圖片" + "顯示");
        showByButton.addActionListener(handler);
        sortByButton = new JButton("排序依據：" + "有效日期");
        sortByButton.addActionListener(handler);
        //設定按鈕大小
        showByButton.setBorder(new EmptyBorder(20, 20, 20, 20));
        sortByButton.setBorder(new EmptyBorder(20, 20, 20, 20));
        topPanel.setLayout(new BorderLayout());
        topPanel.add(showByButton, BorderLayout.WEST);
        topPanel.add(sortByButton, BorderLayout.EAST);

        //------item------

        itemTable = new JTable(foodData, tableColumnNames);


        //---------------------------------secondPanel a.k.a. searchSystemPanel-----------------------------------------
        searchSystem = new SearchSystem();
        zhuyinSymbols = searchSystem.getZhuyinSymbols();
        fromYearComboBox = searchSystem.getFromYearComboBox();
        fromMonthComboBox = searchSystem.getFromMonthComboBox();
        fromDayComboBox = searchSystem.getFromDayComboBox();
        toYearComboBox = searchSystem.getToYearComboBox();
        toMonthComboBox = searchSystem.getToMonthComboBox();
        toDayComboBox = searchSystem.getToDayComboBox();
        searchButton = searchSystem.getSearchButton();
        searchButtonForNameSearch = searchSystem.getSearchButtonForNameSearch();
        clearButton = searchSystem.getClearButton();

        //itemLabel = new JLabel("冰箱內:");
        secondPanel = new JPanel();
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.X_AXIS));
        TestText = new JTextField();
        TestText.setPreferredSize(new Dimension(200, 30));
        TestText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameSearchFlag = true;
                dateSearchFlag = false;
                updateAll();
                TestText.requestFocusInWindow();
                TestText.setCaretPosition(TestText.getText().length());

            }
        });

        nameSearchLabel = new JLabel("依名稱查詢:");
        dateSearchLabel = new JLabel("依到期日查詢:");
        toLabel = new JLabel("到");
        secondPanel.add(nameSearchLabel);
        secondPanel.add(TestText);
        secondPanel.add(searchButtonForNameSearch);
        secondPanel.add(dateSearchLabel);
        secondPanel.add(fromYearComboBox);
        secondPanel.add(fromMonthComboBox);
        secondPanel.add(fromDayComboBox);
        secondPanel.add(toLabel);
        secondPanel.add(toYearComboBox);
        secondPanel.add(toMonthComboBox);
        secondPanel.add(toDayComboBox);
        secondPanel.add(searchButton);
        secondPanel.add(clearButton);

        clearButton.addActionListener(handler);
        searchButton.addActionListener(handler);
        searchButtonForNameSearch.addActionListener(handler);
        fromYearComboBox.addActionListener(handler);
        fromMonthComboBox.addActionListener(handler);
        fromDayComboBox.addActionListener(handler);
        toYearComboBox.addActionListener(handler);
        toMonthComboBox.addActionListener(handler);
        toDayComboBox.addActionListener(handler);


        //--------------------------------------------------------------------------------------------------------------
        itemPanel = new JPanel();

        itemPanel.setLayout(new GridLayout(3,6));
        itemFields = new JLabel[3*6];
        icons = new ImageIcon[3*6];
        //導入item圖片
        for (int i = 0; itemFields[i] != null; i++) {
            itemPanel.add(itemFields[i]);
        }
        itemViewer = itemPanel;
        viewPanel = new JPanel();
        viewPanel.setBackground(Color.black);

        viewPanel.add(itemViewer);
        scrollPane = new JScrollPane(viewPanel);



        //------create------


        JTextField titleName = new JTextField("名稱");
        JTextField titleType = new JTextField("類型");
        JTextField titleDate = new JTextField("有效日期");
        JTextField titleIcon = new JTextField("圖片");
        titleName.setEditable(false);
        titleType.setEditable(false);
        titleDate.setEditable(false);
        titleIcon.setEditable(false);
        newItemName = new JTextField();
        newItemType = new JTextField();
        newItemDate = new JTextField("yyyy/MM/dd");
        newItemIcon = new JList<String>();
        newItemIcon.setCellRenderer(new ItemIconRenderer());
        iconScrollPane = new JScrollPane(newItemIcon);

        createLabel = new JLabel("新增物品到冰箱:");
        createPanel = new JPanel();
        createPanel.setLayout(new GridLayout(2, 4));
        createPanel.add(titleName);
        createPanel.add(titleType);
        createPanel.add(titleDate);
        createPanel.add(titleIcon);
        createPanel.add(newItemName);
        createPanel.add(newItemType);
        createPanel.add(newItemDate);
        createPanel.add(iconScrollPane);

        //------bottom------
        bottomLabel = new JLabel("底部(test)");
        bottomPanel = new JPanel();
        photoPanel = new JPanel();
        photoPanel.setLayout(new GridLayout(1, 4));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        Date nowTime = new Date();
        timeField = new JTextField(nowTime.toString());
        timeField.setEditable(false);
        timeField.setText("現在日期：" + dateFormat.format(today));
        createItemButton = new JButton("新增紀錄");
        createItemButton.setSize(20, 20);
        createItemButton.addActionListener(handler);
        deleteItemButton = new JButton("刪除紀錄");
        deleteItemButton.setSize(20, 20);
        deleteItemButton.addActionListener(handler);
        buttonPanel.add(createItemButton, BorderLayout.EAST);
        buttonPanel.add(deleteItemButton, BorderLayout.WEST);
        uploadPhotoButton = new JButton("上傳照片");
        uploadPhotoButton.addActionListener(handler);
        deletePhotoButton = new JButton("刪除照片");
        deletePhotoButton.addActionListener(handler);
        photoPanel.add(uploadPhotoButton);
        photoPanel.add(deletePhotoButton);           
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(timeField, BorderLayout.WEST);
        bottomPanel.add(buttonPanel);
        bottomPanel.add(photoPanel);

        //------帶進outer------
        updateAll();

        //每隔1小時判斷是否有東西要過期
        Timer t = new Timer();
        long delay = 0L;
        long period = 3600000L;
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TIMER START");
                recordSystem.expiriedNotify();
            }
        }, 0L, period);
    }

    //重整
    private void updateAll() {
        while (true) {
            try {
                readFile();
                readPhotoList();
                break;
            } catch (NoSuchFileException e) {
                try {
                    new File(RECORDS_DIR).mkdirs();
                    File records = new File(RECORDS_FILE);
                    records.createNewFile();
                    new File(PHOTOLIST_DIR).mkdirs();
                    File photos = new File(PHOTOLIST_FILE);
                    photos.createNewFile();

                } catch (IOException iOException) {
                    System.out.println("An error occurred.");
                    iOException.printStackTrace();
                }
            }
        }

        readFoods();
        remove(outerPanel);
        scrollPane.removeAll();
        itemPanel.removeAll();
        itemFields=new JLabel[3*6];
        outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(8, 1));
        outerPanel.add(topPanel);
        //-----------------------------------------secondPanel field----------------------------------------------------
        outerPanel.add(secondPanel);
        secondPanel.removeAll();
        secondPanel.add(nameSearchLabel);
        secondPanel.add(TestText);
        secondPanel.add(searchButtonForNameSearch);
        secondPanel.add(dateSearchLabel);
        secondPanel.add(fromYearComboBox);
        secondPanel.add(fromMonthComboBox);
        secondPanel.add(fromDayComboBox);
        secondPanel.add(toLabel);
        secondPanel.add(toYearComboBox);
        secondPanel.add(toMonthComboBox);
        secondPanel.add(toDayComboBox);
        secondPanel.add(searchButton);
        secondPanel.add(clearButton);
        //--------------------------------------------------------------------------------------------------------------
        itemPanel = new JPanel();

        itemPanel.setLayout(new GridLayout(3,6));
        searchFoodList = new ArrayList<Food>();


        if(nameSearchFlag == true || dateSearchFlag == true) {
            switch (sortMethod) {
                case "類型":
                    System.out.println("類型排序");
                    searchFoodList.sort(Comparator.comparing(Food::getType));
                    break;
                case "有效日期":
                    System.out.println("date sort");
                    searchFoodList.sort(Comparator.comparing(Food::getExpiryDate));
                    break;
            }
            foodList = recordSystem.getFoods();
            int cnt = 0;
            if(nameSearchFlag) {
                for (int i = 0; i < foodList.size(); ++i) {
                    if (foodList.get(i).getName().contains(TestText.getText())) {
                        cnt = cnt + 1;
                    }
                }
            }
            else{
                for (int i = 0; i < foodList.size(); ++i) {
                    Date tmpDate = foodList.get(i).getExpiryDate();
                    Date searchStartDate = searchSystem.getFromDate();
                    Date searchEndDate = searchSystem.getToDate();
                    if ((tmpDate.compareTo(searchStartDate) > 0 || tmpDate.compareTo(searchStartDate) == 0) && (tmpDate.compareTo(searchEndDate) == 0|| tmpDate.compareTo(searchEndDate) < 0)) {
                        cnt = cnt + 1;
                    }
                }
            }

            searchFoodData = new String[cnt][];
            int k = 0;
            if(nameSearchFlag) {
                for (int i = 0; i < foodList.size(); ++i) {
                    if (foodList.get(i).getName().contains(TestText.getText())) {
                        String[] data = {foodList.get(i).getName(), foodList.get(i).getType(), foodList.get(i).getExpiryDate().toString()};
                        searchFoodList.add(foodList.get(i));
                        searchFoodData[k] = data;
                        k = k + 1;
                    }
                }
            }
            else{
                for (int i = 0; i < foodList.size(); ++i) {
                    Date tmpDate = foodList.get(i).getExpiryDate();
                    Date searchStartDate = searchSystem.getFromDate();
                    Date searchEndDate = searchSystem.getToDate();
                    if ((tmpDate.compareTo(searchStartDate) > 0 || tmpDate.compareTo(searchStartDate) == 0) && (tmpDate.compareTo(searchEndDate) == 0|| tmpDate.compareTo(searchEndDate) < 0)) {
                        String[] data = {foodList.get(i).getName(), foodList.get(i).getType(), foodList.get(i).getExpiryDate().toString()};
                        searchFoodList.add(foodList.get(i));
                        searchFoodData[k] = data;
                        k = k + 1;
                    }
                }
            }
        }



        switch (displayMethod) {
            case "圖片":
                //--------------------------------------------
                if (nameSearchFlag == true || dateSearchFlag == true) {
                    for (int i = 0; i <  searchFoodList.size(); ++i) {

                        Icon tmp = createIcon(searchFoodList.get(i).getIcon());
                        itemFields[i]=new JLabel(tmp);
                    }

                }
                else {
                    for (int i = 0; i < foodList.size(); ++i) {

                        Icon tmp = createIcon(foodList.get(i).getIcon());
                        itemFields[i] = new JLabel(tmp);
                    }
                }
                //-------------------------------------------------------
                for(int i = 0;i<3*6;i++) {
                    if(itemFields[i]!=null){
                        itemPanel.add(itemFields[i]);
                    }else{
                        itemPanel.add(new JLabel());
                    }

                }

                scrollPane = new JScrollPane(itemPanel);
                break;
            case "表格":
                //--------------------------------------
                if(nameSearchFlag == true || dateSearchFlag == true){
                    itemTable = new JTable(searchFoodData, tableColumnNames);
                }
                else{
                    itemTable = new JTable(foodData, tableColumnNames);
                }
                if(itemTable == null){
                    itemTable =  new JTable(foodData, tableColumnNames);
                }
                //itemTable = new JTable(foodData, tableColumnNames);
                //---------------------------------------------
                itemTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
                    @Override
                    public Component getTableCellRendererComponent(JTable table,
                            Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                        ArrayList<Integer> warning_indices = recordSystem.warningFoodsIndices();
                        System.out.printf("Size of warning: %d, and row is %d\n", warning_indices.size(), row);
                        if (warning_indices.contains(row)) {
                            setBackground(table.getBackground());
                            setForeground(Color.RED);
                        }
                        else if(isSelected){
                            c.setBackground(Color.YELLOW);
                        }
                        else {
                            setBackground(table.getBackground());
                            setForeground(table.getForeground());
                        }
                        return this;
                    }   
                });
                scrollPane = new JScrollPane(itemTable);
                break;
        }





        outerPanel.add(scrollPane);
        outerPanel.add(createLabel);
        outerPanel.add(createPanel);
        outerPanel.add(buttonPanel);
        outerPanel.add(bottomLabel);
        outerPanel.add(bottomPanel);
        add(outerPanel);
        //帶進GUI------
//
        SwingUtilities.updateComponentTreeUI(this);

    }

    public void readFoods() {

        foodList = recordSystem.getFoods();
        foodData = new String[foodList.size()][];

        System.out.println("SIZE:" + (foodList.size()));
        for (int i = 0; i < foodList.size(); ++i) {
            String[] data = {foodList.get(i).getName(), foodList.get(i).getType(), foodList.get(i).getExpiryDate().toString()};


            foodData[i] = data;
            System.out.println(foodData[i][0]);
        }
    }

    public void createFood(String name, String type, String date_str, String icon_name) {
        try {
            System.out.println("icon name:" + icon_name);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = simpleDateFormat.parse(date_str);

            recordSystem.createFood(name, type, icon_name, date);
            Food item = recordSystem.getFood(item_counts);


            readFoods();
        } catch (ParseException parseException) {
            System.out.println(parseException.getMessage());
            System.out.println("DD");
        }
    }

    public void deleteFood(int index) {
        recordSystem.deleteFood(index);
    }

    private void readFile() throws NoSuchFileException {
        System.out.println("嘗試讀取 records.txt");
        try (Scanner input = new Scanner(Paths.get(RECORDS_FILE))) {
            // read record from file
            recordSystem.clearFoods();
            while (input.hasNext()) { // while there is more to read
                createFood(input.next(), input.next(), input.next(), input.next());
            }
        } catch (NoSuchFileException noSuchFileException) {
            throw new NoSuchFileException(RECORDS_FILE);
        } catch (IOException | NoSuchElementException |
                 IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void writeFile() throws FileNotFoundException {
        System.out.println("嘗試寫入檔案");
        try (Formatter output = new Formatter(RECORDS_FILE)) {
            try {
                // output new record to file; assumes valid input
                for (int i = 0; i < foodList.size(); i++) {
                    String name = foodList.get(i).getName();
                    String type = foodList.get(i).getType();
                    String date_str = dateFormat.format(foodList.get(i).getExpiryDate());
                    String icon_name = foodList.get(i).getIcon();
                    output.format("%s %s %s %s%n", name, type, date_str, icon_name);
                }


            } catch (NoSuchElementException elementException) {
                System.err.println("Invalid input. Please try again.");
            } catch (SecurityException |
                     FormatterClosedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readPhotoList() throws NoSuchFileException {
        System.out.println("嘗試讀檔");
        try (Scanner input = new Scanner(Paths.get(PHOTOLIST_FILE))) {
            // read record from file
            photoList = new ArrayList<String>();
            while (input.hasNextLine()) { // while there is more to read
                photoList.add(input.nextLine());
            }
            String[] tmp = photoList.toArray(new String[0]);
            newItemIcon.setListData(tmp);
        } catch (NoSuchFileException noSuchFileException) {
            throw new NoSuchFileException(PHOTOLIST_FILE);
        } catch (IOException | NoSuchElementException |
                 IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void writePhotoList() throws FileNotFoundException {
        System.out.println("嘗試寫入檔案");
        try (Formatter output = new Formatter(PHOTOLIST_FILE)) {
            try {
                // output new record to file; assumes valid input
                for (int i = 0; i < photoList.size(); i++) {
                    output.format("%s%n", photoList.get(i));
                }

            } catch (NoSuchElementException elementException) {
                System.err.println("Invalid input. Please try again.");
            } catch (SecurityException |
                     FormatterClosedException e) {
                e.printStackTrace();
            }
        }
    }

    private void deletePhotoFile(String photoName){
         File file = new File(DESTINATION_DIR+photoName);
 
        boolean result = file.delete();
        if (result) {
            System.out.println("File is successfully deleted.");
        }
        else {
            System.out.println("File deletion failed.");
        }
    }
    private void uploadSecondPanel(){
        //outerPanel.add(secondPanel);
        secondPanel.removeAll();
        secondPanel.add(nameSearchLabel);
        secondPanel.add(TestText);
        secondPanel.add(searchButtonForNameSearch);
        secondPanel.add(dateSearchLabel);
        secondPanel.add(fromYearComboBox);
        secondPanel.add(fromMonthComboBox);
        secondPanel.add(fromDayComboBox);
        secondPanel.add(toLabel);
        secondPanel.add(toYearComboBox);
        secondPanel.add(toMonthComboBox);
        secondPanel.add(toDayComboBox);
        secondPanel.add(searchButton);
        secondPanel.add(clearButton);
    }
    private void deletePhoto()  {
        int photoIndex = newItemIcon.getSelectedIndex();
        if(photoList!=null && photoIndex >= 0){
            String photoName = photoList.get(photoIndex);
            deletePhotoFile(photoName);
            photoList.remove(photoIndex);
            try{
                writePhotoList();
                readPhotoList();
            }catch(FileNotFoundException | NoSuchFileException e){
                    e.printStackTrace();
            }
        }

    }

    private Icon createIcon(String icon_name){
        ImageIcon icon = new ImageIcon(DESTINATION_DIR + "/" + icon_name);
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(40  , 40, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        return  icon;
    }
    private void displaySwitcher() //切換顯示方式
    {
        // TODO
        scrollPane.remove(itemViewer);
        switch (displayMethod) {
            case "圖片":
                displayMethod = "表格";
                showByButton.setText("以" + displayMethod + "顯示");
                System.out.println("切到表格顯示");
                break;
            case "表格":
                displayMethod = "圖片";
                showByButton.setText("以" + "圖片" + "顯示");
                System.out.println("切到圖片顯示");
                break;
        }
        updateAll();
    }

    private void sortSwitcher()//切換排序方式
    {
        // TODO

        sortByButton.setText("排序依據：" + sortMethod);
        switch (sortMethod) {
            case "類型":
                sortMethod = "有效日期";
                sortByButton.setText("排序依據：" + sortMethod);
                System.out.println("切到以日期排序");
                break;
            case "有效日期":
                sortMethod = "類型";
                sortByButton.setText("排序依據：" + sortMethod);
                System.out.println("切到以類型排序");
                break;
        }
        recordSystem.sortFood(sortMethod);
        try{
            writeFile();
            updateAll();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
    }

    private void createAction() throws FileNotFoundException //新增紀錄
    {
        // TODO
        String name = newItemName.getText();
        String type = newItemType.getText();
        String date_str = newItemDate.getText();
        String icon_name = newItemIcon.getSelectedValue();
        if(name != null && type != null && date_str != null && icon_name != null  ){
            createFood(name, type, date_str, icon_name);
            recordSystem.expiriedNotify();
            writeFile();
            updateAll();
        }
//            String icon_path = newItemIcon.getText();
        

    }

    private void deleteAction() throws FileNotFoundException //刪除紀錄
    {
        // TODO
        if (itemTable.getSelectedRow() != -1) {
            deleteFood(itemTable.getSelectedRow());
            readFoods();
            writeFile();
            updateAll();
        }
    }

    //上傳照片
    public void uploadAction() {
        JFileChooser fileChooser = new JFileChooser();//宣告filechooser
        fileChooser.addChoosableFileFilter(new ImageFilter());//只能選照片
        fileChooser.setAcceptAllFileFilterUsed(false);//只能選照片
        int returnValue = fileChooser.showOpenDialog(null);//叫出filechooser
        if (returnValue == JFileChooser.APPROVE_OPTION) //判斷是否選擇檔案
        {
            try {
                File selectedFile = fileChooser.getSelectedFile();//指派給File

                System.out.println(selectedFile.getName()); //印出檔名 
                recordSystem.uploadPhoto(selectedFile.getPath());//上傳照片
                photoList.add(selectedFile.getName());
                writePhotoList();
                readPhotoList();
            } catch (IOException | SecurityException |
                     FormatterClosedException err) {
                err.printStackTrace();
            }
        }
    }
    private boolean containsAllCharacters(String str, Set<Character> zhuyinSymbols) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (zhuyinSymbols.contains(c)) {
                return true;
            }
        }
        return false;
    }






    private class Handler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == searchButton){
                TestText.setText("");
                int fromYearNumber = searchSystem.getFromYearNumber();
                int fromMonthNumber = searchSystem.getFromMonthNumber();
                int fromDayNumber = searchSystem.getFromDayNumber();
                int toYearNumber = searchSystem.getToYearNumber();
                int toMonthNumber = searchSystem.getToMonthNumber();
                int toDayNumber = searchSystem.getToDayNumber();
                Date fromDate = new Date(fromYearNumber, fromMonthNumber, fromDayNumber);
                Date toDate = new Date(toYearNumber, toMonthNumber, toMonthNumber);
                if (fromDate.compareTo(toDate) < 0) {
                    System.out.println("fromDate 在 toDate 之前");
                    dateSearchFlag = true;
                    nameSearchFlag = false;
                    updateAll();
                    //dateSearchFlag = false;

                } else if (fromDate.compareTo(toDate) > 0) {
                    JOptionPane.showMessageDialog(null, "開始日期須小於或等於結束日期", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    dateSearchFlag = true;
                    nameSearchFlag = false;
                    updateAll();
                    System.out.println("fromDate 和 toDate 相同");
                    //dateSearchFlag = false;
                }

            }
            else if(e.getSource() == searchButtonForNameSearch){
                nameSearchFlag = true;
                dateSearchFlag = false;
                updateAll();
                TestText.requestFocusInWindow();
                TestText.setCaretPosition(TestText.getText().length());
            }
            else if(e.getSource() == clearButton){
                dateSearchFlag = false;
                nameSearchFlag = false;
                updateAll();
            }

            else if (e.getSource() == showByButton) {
                displaySwitcher();
            } else if (e.getSource() == sortByButton) {
                sortSwitcher();
            } else if (e.getSource() == createItemButton) {
                try {
                    createAction();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getSource() == deleteItemButton) {
                try {
                    deleteAction();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getSource() == uploadPhotoButton) {
                uploadAction();
            }else if(e.getSource() == deletePhotoButton){
                deletePhoto();
            }
            else if(e.getSource() == fromYearComboBox){
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                int selectedItem = (Integer) source.getSelectedItem();
                int selectedMonth = (Integer) fromMonthComboBox.getSelectedItem();
                int selectedYearNumber = -1;
                int selectedMonthNumber = -1;
                try {
                    selectedYearNumber = selectedItem;
                    selectedMonthNumber = selectedMonth;
                    System.out.println("Selected year: " + selectedYearNumber);
                    System.out.println("Selected month: " + selectedMonthNumber);
                } catch (NumberFormatException ex) {
                    System.out.println("Selected item is not a valid number: " + selectedItem);
                }

                fromDayComboBox = searchSystem.createDayComboBox(selectedYearNumber, selectedMonthNumber);
                fromDayComboBox.addActionListener(this);
                searchSystem.setFromDayComboBox(fromDayComboBox);
                updateAll();

            }

            else if(e.getSource() == toYearComboBox){
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                int selectedItem = (Integer) source.getSelectedItem();
                int selectedMonth = (Integer) toMonthComboBox.getSelectedItem();
                int selectedYearNumber = -1;
                int selectedMonthNumber = -1;
                try {
                    selectedYearNumber = selectedItem;
                    selectedMonthNumber = selectedMonth;
                    System.out.println("Selected year: " + selectedYearNumber);
                    System.out.println("Selected month: " + selectedMonthNumber);
                } catch (NumberFormatException ex) {
                    System.out.println("Selected item is not a valid number: " + selectedItem);
                }

                toDayComboBox = searchSystem.createDayComboBox(selectedYearNumber, selectedMonthNumber);
                toDayComboBox.addActionListener(this);
                searchSystem.setToDayComboBox(toDayComboBox);
                updateAll();


            }
            else if(e.getSource() == fromMonthComboBox){
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                int selectedItem = (Integer) source.getSelectedItem();
                int selectedYear = (Integer) toYearComboBox.getSelectedItem();
                int selectedYearNumber = -1;
                int selectedMonthNumber = -1;
                try {
                    selectedYearNumber = selectedYear;
                    selectedMonthNumber = selectedItem;
                    System.out.println("Selected year: " + selectedYearNumber);
                    System.out.println("Selected month: " + selectedMonthNumber);
                } catch (NumberFormatException ex) {
                    System.out.println("Selected item is not a valid number: " + selectedItem);
                }
                fromDayComboBox = searchSystem.createDayComboBox(selectedYearNumber, selectedMonthNumber);
                fromDayComboBox.addActionListener(this);
                searchSystem.setFromDayComboBox(fromDayComboBox);
                updateAll();
            }
            else if(e.getSource() == toMonthComboBox){
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                int selectedItem = (Integer)source.getSelectedItem();
                int selectedYear = (Integer)toYearComboBox.getSelectedItem();
                int selectedYearNumber = -1;
                int selectedMonthNumber = -1;
                try {
                    selectedYearNumber = selectedYear;
                    selectedMonthNumber = selectedItem;
                    System.out.println("Selected year: " + selectedYearNumber);
                    System.out.println("Selected month: " + selectedMonthNumber);
                } catch (NumberFormatException ex) {
                    System.out.println("Selected item is not a valid number: " + selectedItem);
                }
                toDayComboBox = searchSystem.createDayComboBox(selectedYearNumber, selectedMonthNumber);
                toDayComboBox.addActionListener(this);
                searchSystem.setToDayComboBox(toDayComboBox);
                updateAll();
            }
            else if(e.getSource() == fromDayComboBox){

            }
            else if(e.getSource() == toDayComboBox){

            }
        }
    }
}