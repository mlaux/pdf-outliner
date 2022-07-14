package com.matthewlaux.outliner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SettingsPanel implements ChangeListener {
    private JSlider newlineX;
    private JSlider spaceX;
    private JSlider paragraphSpace;
    private JSlider columnMin;
    private JSlider columnMax;
    private JCheckBox showBBoxes;
    private JPanel panel;

    private SettingsChangeListener settingsChangeListener;
    private static final Settings settings = new Settings();

    static class Settings {
        public int newlineX = 100;
        public int spaceX = 5;
        public int paragraphSpacing = 26;
        public int columnMin = 5;
        public int columnMax = 50;
        public boolean showBBoxes = true;

        private Settings() {

        }
    }

    public SettingsPanel() {
        newlineX.addChangeListener(this);
        spaceX.addChangeListener(this);
        paragraphSpace.addChangeListener(this);
        columnMin.addChangeListener(this);
        columnMax.addChangeListener(this);
        showBBoxes.addChangeListener(this);
    }

    public static Settings getSettings() {
        return settings;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setSettingsChangeListener(SettingsChangeListener settingsChangeListener) {
        this.settingsChangeListener = settingsChangeListener;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider && ((JSlider) e.getSource()).getValueIsAdjusting()) {
            return;
        }

        if (settingsChangeListener == null) {
            return;
        }

        settings.newlineX = newlineX.getValue();
        settings.spaceX = spaceX.getValue();
        settings.paragraphSpacing = paragraphSpace.getValue();
        settings.columnMin = columnMin.getValue();
        settings.columnMax = columnMax.getValue();
        settings.showBBoxes = showBBoxes.isSelected();
        settingsChangeListener.onSettingsChange(settings);
    }

    interface SettingsChangeListener {
        void onSettingsChange(Settings settings);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        final JLabel label1 = new JLabel();
        label1.setText("Newline x distance");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Space x distance");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Paragraph line spacing");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Horizontal column min");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label4, gbc);
        newlineX = new JSlider();
        newlineX.setMaximum(200);
        newlineX.setValue(100);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(newlineX, gbc);
        spaceX = new JSlider();
        spaceX.setMaximum(20);
        spaceX.setPaintLabels(false);
        spaceX.setPaintTicks(false);
        spaceX.setValue(5);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spaceX, gbc);
        paragraphSpace = new JSlider();
        paragraphSpace.setMaximum(30);
        paragraphSpace.setValue(26);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(paragraphSpace, gbc);
        columnMin = new JSlider();
        columnMin.setValue(5);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(columnMin, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Horizontal column max");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label5, gbc);
        columnMax = new JSlider();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(columnMax, gbc);
        showBBoxes = new JCheckBox();
        showBBoxes.setSelected(true);
        showBBoxes.setText("Show boxes?");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showBBoxes, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
