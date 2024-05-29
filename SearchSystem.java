import javax.swing.*;
import java.time.LocalDate;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
public class SearchSystem {
    private JComboBox<Integer> fromYearComboBox;
    private JComboBox<Integer> fromMonthComboBox;
    private JComboBox<Integer> fromDayComboBox;
    private JComboBox<Integer> toYearComboBox;
    private JComboBox<Integer> toMonthComboBox;
    private JComboBox<Integer> toDayComboBox;
    private JLabel daySearchText;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int[] daysInMonth;
    private Integer fromYearNumber;
    private Integer fromMonthNumber;
    private Integer fromDayNumber;
    private Integer toYearNumber;
    private Integer toMonthNumber;
    private Integer toDayNumber;
    private JButton searchButton;
    private JButton clearButton;
    private Date fromDate;
    private Date toDate;
    private boolean SearchDateInvaildFlag;
    private Set<Character> zhuyinSymbols = new HashSet<>();
    private JButton searchButtonForNameSearch;

    public SearchSystem() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        currentYear = today.getYear();
        currentMonth = today.getMonthValue();
        currentDay = today.getDayOfMonth();

        // 初始化组件
        fromYearComboBox = createYearComboBox(2020, 2100);
        fromMonthComboBox = createMonthComboBox();
        fromDayComboBox = createDayComboBox();
        toYearComboBox = createYearComboBox(2020, 2100);
        toMonthComboBox = createMonthComboBox();
        toDayComboBox = createDayComboBox();
        searchButtonForNameSearch = new JButton("Search");
        searchButton = new JButton("Search");
        clearButton = new JButton("Reset");

        daySearchText = new JLabel("TO");
        SearchDateInvaildFlag = false;
        zhuyinSymbols = createZhuyinSymbols();
        //ComboBoxActionListener comboBoxActionListener = new ComboBoxActionListener();
        //fromYearComboBox.addActionListener(comboBoxActionListener);
        //fromMonthComboBox.addActionListener(comboBoxActionListener);
        //fromDayComboBox.addActionListener(comboBoxActionListener);
        //toYearComboBox.addActionListener(comboBoxActionListener);
        //toMonthComboBox.addActionListener(comboBoxActionListener);
        //toDayComboBox.addActionListener(comboBoxActionListener);
        //searchButton.addActionListener(comboBoxActionListener);
    }

    public Set<Character> createZhuyinSymbols() {
        Set<Character> zhuyinSymbols = new HashSet<>();
        for (char c = '\u3105'; c <= '\u3129'; c++) {
            zhuyinSymbols.add(c);
        }
        return zhuyinSymbols;
    }

    public Set<Character> getZhuyinSymbols() {
        return zhuyinSymbols;
    }

    private JComboBox<Integer> createYearComboBox(int startYear, int endYear) {
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        for (int i = startYear; i <= endYear; i++) {
            yearComboBox.addItem(i);
        }
        return yearComboBox;
    }

    private JComboBox<Integer> createMonthComboBox() {
        JComboBox<Integer> monthComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }
        return monthComboBox;
    }

    public JComboBox<Integer> createDayComboBox(int year, int month) {
        daysInMonth = getDaysInMonthArray(year);
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= daysInMonth[month-1]; i++) {
            dayComboBox.addItem(i);
        }

        return dayComboBox;
    }
    public void setToDayComboBox(JComboBox<Integer> a) {
        this.toDayComboBox = a;
    }
    public void setFromDayComboBox(JComboBox<Integer> a) {
        this.fromDayComboBox = a;
    }
    public JComboBox<Integer> createDayComboBox() {
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(i);
        }
        return dayComboBox;
    }

    // 获取 from 相关 ComboBox 的方法
    public JComboBox<Integer> getFromYearComboBox() {
        return fromYearComboBox;
    }

    public JComboBox<Integer> getFromMonthComboBox() {
        return fromMonthComboBox;
    }

    public JComboBox<Integer> getFromDayComboBox() {
        return fromDayComboBox;
    }

    // 获取 to 相关 ComboBox 的方法
    public JComboBox<Integer> getToYearComboBox() {
        return toYearComboBox;
    }

    public JComboBox<Integer> getToMonthComboBox() {
        return toMonthComboBox;
    }

    public JComboBox<Integer> getToDayComboBox() {
        return toDayComboBox;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getSearchButtonForNameSearch() {
        return searchButtonForNameSearch;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public int[] getDaysInMonthArray(int year) {
        int[] daysInMonth = {31, isLeapYear(year) ? 29 : 28, 31, 30,31,30,31,31,30,31,30,31};
        return daysInMonth;
    }
    public boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public int getFromDayNumber() {
        fromDayNumber = (Integer) fromDayComboBox.getSelectedItem();
        //System.out.println(fromDayNumber.intValue() + "FD");
        return fromDayNumber.intValue();
    }

    public int getFromMonthNumber() {
        fromMonthNumber = (Integer) fromMonthComboBox.getSelectedItem();
        //System.out.println(fromMonthNumber.intValue() + "FM");
        return fromMonthNumber.intValue();
    }

    public int getFromYearNumber() {
        fromYearNumber = (Integer) fromYearComboBox.getSelectedItem();
        //System.out.println(fromYearNumber.intValue() + "FY");
        return fromYearNumber.intValue();
    }

    public int getToDayNumber() {
        toDayNumber = (Integer) toDayComboBox.getSelectedItem();
        System.out.println(toDayNumber.intValue() + "TD");
        return toDayNumber.intValue();
    }

    public int getToMonthNumber() {
        toMonthNumber = (Integer) toMonthComboBox.getSelectedItem();
        //System.out.println(toMonthNumber.intValue() + "TM");
        return toMonthNumber.intValue();
    }

    public int getToYearNumber() {
        toYearNumber = (Integer) toYearComboBox.getSelectedItem();
        //System.out.println(toYearNumber.intValue()  + "TY");
        return toYearNumber.intValue();
    }

    public Date getFromDate() {
        fromDate = new Date(getFromYearNumber() - 1900, getFromMonthNumber() - 1, getFromDayNumber());
        return fromDate;
    }

    public Date getToDate() {
        toDate = new Date(getToYearNumber() - 1900, getToMonthNumber() - 1, getToDayNumber());
        return toDate;
    }
    /**
    class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(e.getSource() == searchButton){
                fromYearNumber = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                fromMonthNumber = Integer.parseInt((String) fromMonthComboBox.getSelectedItem());
                fromDayNumber = Integer.parseInt((String) fromDayComboBox.getSelectedItem());
                toYearNumber = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                toMonthNumber = Integer.parseInt((String) toMonthComboBox.getSelectedItem());
                toDayNumber = Integer.parseInt((String) toDayComboBox.getSelectedItem());a
                fromDate = new Date(fromYearNumber, fromMonthNumber, fromDayNumber);
                toDate = new Date(toYearNumber, toMonthNumber, toMonthNumber);
                if (fromDate.compareTo(toDate) < 0) {
                    System.out.println("fromDate 在 toDate 之前");
                } else if (fromDate.compareTo(toDate) > 0) {
                    System.out.println("fromDate 在 toDate 之後");
                } else {
                    System.out.println("fromDate 和 toDate 相同");
                }
            }

           if(e.getSource() == fromYearComboBox){
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

                fromDayComboBox = createDayComboBox(selectedYearNumber, selectedMonthNumber);

            }

            if(e.getSource() == toYearComboBox){
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

                toDayComboBox = createDayComboBox(selectedYearNumber, selectedMonthNumber);


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
                fromDayComboBox = createDayComboBox(selectedYearNumber, selectedMonthNumber);
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
                toDayComboBox = createDayComboBox(selectedYearNumber, selectedMonthNumber);
            }
            else if(e.getSource() == fromDayComboBox){

            }
            else if(e.getSource() == toDayComboBox){

            }


        }
             */

}
