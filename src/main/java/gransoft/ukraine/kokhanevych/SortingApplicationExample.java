package gransoft.ukraine.kokhanevych;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class SortingApplicationExample {
    /**
     * Declaration JFame variable.
     */
    private static JFrame frame;
    /**
     * Declaration JPanel variable for the main page.
     */
    private static JPanel sortScreen;
    /**
     * Declaration Integer inputValue variable.
     */
    private static Integer inputValue;
    /**
     * Declaration JPanel variable for main part of main page with buttons.
     */
    private static JPanel numericButtonsScreen;

    /**
     * Declaration Random service variable.
     */
    private static Random random = new Random();

    /**
     * Declaration JPanel introduction page.
     */
    private static JPanel introductionScreen;

    /**
     * Entry point for all program. This is the main method which initialize jframe
     * and use initPage() method.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = getFrame();
            createInitPage();
        });
    }

    /**
     * This method is used for initialize and configure main frame and add welcome panel.
     */
    private static void createInitPage() {
        introductionScreen = new JPanel();
        frame.add(introductionScreen);

        introductionScreen.setLayout(null);

        JLabel header = new JLabel("How many numbers to display?");
        header.setBounds(300, 250, 200, 20);
        introductionScreen.add(header);

        JTextField inputField = new JTextField(5);
        inputField.setBounds(300, 280, 180, 30);
        introductionScreen.add(inputField);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(350, 330, 80, 30);
        introductionScreen.add(enterButton);
        enterButton.addActionListener(e -> {
            if (validate(inputField.getText())) {
                inputValue = Integer.parseInt(inputField.getText());
                frame.remove(introductionScreen);
                createSortPage(inputValue);
            }
        });
    }

    /**
     * This method is used to initialize and configure main page. Page consist from
     * main panel where displayed common buttons and side panel which contains two
     * manipulation buttons.
     *
     * @param input This is a value of buttons which was inputed by user from input
     *              field from welcome page.
     */
    private static void createSortPage(Integer input) {
        sortScreen = new JPanel();
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(20);
        sortScreen.setLayout(borderLayout);
        frame.add(sortScreen);

        numericButtonsScreen = new JPanel();
        numericButtonsScreen.setLayout(new GridBagLayout());

        JPanel sidePanel = new JPanel();
        GridLayout sideGridLayout = new GridLayout(2, 0);
        sideGridLayout.setVgap(40);
        sidePanel.setLayout(sideGridLayout);
        sortScreen.add(sidePanel, BorderLayout.EAST);
        sortScreen.add(numericButtonsScreen, BorderLayout.CENTER);

        JButton sortButton = new JButton("Sort");
        JButton resetButton = new JButton("Reset");

        sidePanel.add(sortButton);
        sidePanel.add(resetButton);


        resetButton.addActionListener(e -> {
            frame.remove(sortScreen);
            createInitPage();
            introductionScreen.revalidate();
        });
        final List<JButton> numericButtons = createButtons(input);
        int i = random.nextInt(input);
        numericButtons.get(i).setText(String.valueOf(random.nextInt(30 - 1) + 1));

        sortButton.addActionListener(new ActionListener() {
            boolean isAscendingOrder = true;

            private void setEnableComponents(boolean enable) {
                sortButton.setEnabled(enable);
                resetButton.setEnabled(enable);
                Component[] panelComponents = numericButtonsScreen.getComponents();
                for (Component component : panelComponents) {
                    component.setEnabled(enable);
                }
            }

            private void quickSort() {
                setEnableComponents(false);
                quickSortImpl(isAscendingOrder, numericButtons, 0, numericButtons.size() - 1);
                isAscendingOrder = !isAscendingOrder;
                setEnableComponents(true);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(this::quickSort).start();
            }
        });
    }

    /**
     * This method is used quick sort algorithm implementation.
     *
     * @param sortDirection boolean value which help identify a current order in array.
     * @param buttons       Array of button needed to sort.
     * @param begin         Index of first element from array.
     * @param end           Index of last element from array.
     */
    private static void quickSortImpl(boolean sortDirection, List<JButton> buttons, int begin, int end) {
        if (begin < end) {
            int separatorIndex = quickSortSeparator(sortDirection, buttons, begin, end);

            quickSortImpl(sortDirection, buttons, begin, separatorIndex);
            quickSortImpl(sortDirection, buttons, separatorIndex + 1, end);
        }
    }

    /**
     * This is a service method witch get button value and convert it to integer.
     *
     * @param button Input button.
     * @return Integer value of button.
     */
    private static int getButtonNumber(JButton button) {
        return Integer.parseInt(button.getText());
    }

    /**
     * This is a service method which used in quick sort algorithm. This is a
     * implementation for ascending order sort.
     *
     * @param isAscendingOrder boolean value which help identify a current order in array.
     * @param buttons          Array of button needed to sort.
     * @param begin            Index of first element from array.
     * @param end              Index of last element from array.
     * @return special index needed for quick sort algorithm.
     */
    private static int quickSortSeparator(boolean isAscendingOrder, List<JButton> buttons, int begin, int end) {
        int pivot = getButtonNumber(buttons.get(begin));
        int i = begin;
        int j = end;
        while (true) {
            if (isAscendingOrder) {
                while (getButtonNumber(buttons.get(i)) < pivot) {
                    i++;
                }
                while (getButtonNumber(buttons.get(j)) > pivot) {
                    j--;
                }
            } else {
                while (getButtonNumber(buttons.get(i)) > pivot) {
                    i++;
                }
                while (getButtonNumber(buttons.get(j)) < pivot) {
                    j--;
                }
            }

            if (i < j) {
                int finalI = i;
                int finalJ = j;
                SwingUtilities.invokeLater(() -> {
                    int swapTemp = getButtonNumber(buttons.get(finalI));
                    buttons.get(finalI).setText(buttons.get(finalJ).getText());
                    buttons.get(finalJ).setText(Integer.toString(swapTemp));
                });
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                j--;
            } else {
                return j;
            }
        }
    }

    /**
     * This method is used for configuration frame.
     *
     * @return configured frame.
     */
    private static JFrame getFrame() {
        JFrame frame = new JFrame("Test task");
        frame.setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        frame.setBounds(dimension.width / 2 - 400, dimension.height / 2 - 300, 800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    /**
     * This method is used for create and add to main panel some quantity of buttons.
     *
     * @param inputValue Quantity of buttons.
     * @return array of buttons.
     */
    private static List<JButton> createButtons(final Integer inputValue) {
        List<JButton> numericButtonsList = new ArrayList<>();
        int gridx = 0;
        int gridy = 0;
        for (int i = 0; i < inputValue; i++) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridx = gridx;
            constraints.gridy = gridy;

            gridy++;
            if (gridy >= 10) {
                gridy = 0;
                gridx++;
            }
            final JButton numericButton = new JButton(getRandomNumber());
            numericButtonsScreen.add(numericButton, constraints);
            numericButtonsList.add(numericButton);

            numericButton.addActionListener(e -> {
                if (getButtonNumber(numericButton) > 30) {
                    JOptionPane.showMessageDialog(null,
                            "Please, select a value smaller or equal to 30", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    frame.remove(sortScreen);
                    createSortPage(getButtonNumber(numericButton));
                }
            });
        }
        return numericButtonsList;
    }

    /**
     * This method is used for add each button random number from 1 to 1000.
     *
     * @return string representation of number.
     */
    private static String getRandomNumber() {
        int randomNumber = random.nextInt(1001 - 1) + 1;
        return Integer.toString(randomNumber);
    }

    /**
     * This method is used for validation input value from console.
     *
     * @param inputValue value from console.
     * @return boolean result of validation.
     */
    private static boolean validate(String inputValue) {
        if (!inputValue.isEmpty() && isNumber(inputValue)) {
            return true;
        }
        JOptionPane.showMessageDialog(null,
                "Please enter only an integer value between 1 and 1000", "Warning",
                JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    /**
     * Service method which help check that input value is a correct number.
     *
     * @param inputValue Input value which needed in validation.
     * @return boolean result of validation.
     */
    private static boolean isNumber(String inputValue) {
        try {
            int number = Integer.parseInt(inputValue);
            return isRange(number);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Service method which help check that input value is in needed range.
     *
     * @param number Input value which needed in validation.
     * @return boolean result of validation.
     */
    private static boolean isRange(int number) {
        final int upperRange = 0;
        final int lowerRange = 1000;
        if (number <= upperRange || number > lowerRange) {
            return false;
        }
        return true;
    }
}
