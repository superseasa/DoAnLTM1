/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.CPUSchedule.App;

import client.CPUSchedule.Algorithms.ResultAfterExecuteAlgorithm;
import client.CPUSchedule.Constant.Constant;
import static client.CPUSchedule.Control.ProcessTablePanelAction.renderDefaultGraph;
import static client.CPUSchedule.Control.ProcessTablePanelAction.renderGraph;
import client.Client;
import com.google.gson.Gson;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Ram4GB
 */
public class EastPanel_ChooseTypeAlgorithmPanel extends JPanel {

    public static JLabel labelType;
    public static JComboBox<String> typeAlgorithmArr;

    public EastPanel_ChooseTypeAlgorithmPanel() {
        setPreferredSize(new Dimension(Constant.WIDTH_EAST_PANEL, 50));
        setBorder(BorderFactory.createTitledBorder("Choose algorithm type"));

        labelType = new JLabel("Algorithm Type: ");
        typeAlgorithmArr = new JComboBox<>();
        Constant.defaultTypeAlgorithm = "FCFS";

        typeAlgorithmArr.addItem("FCFS");
        typeAlgorithmArr.addItem("SJF"); // không độc quyền 1 tiến trình sẽ chạy hết
        typeAlgorithmArr.addItem("SRT"); // độc quyền của SJF. Tới giây của 1 tiến trình nào đó thì phải bị dừng lại
        typeAlgorithmArr.addItem("RR");
        typeAlgorithmArr.addItem("PP");  // độc quyền
        typeAlgorithmArr.addItem("PNP"); // không độc quyền
        typeAlgorithmArr.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent item) {
                Constant.defaultTypeAlgorithm = item.getItem().toString();

                if (!Constant.defaultTypeAlgorithm.isEmpty()) {
                    try {
                        System.out.println(Constant.defaultTypeAlgorithm);
                        if (Constant.defaultTypeAlgorithm.equals("PP") || Constant.defaultTypeAlgorithm.equals("PNP")) {
                            System.out.println("thuat toan " + Constant.defaultTypeAlgorithm);
                            EastPanel_AddProcessPanel.textFieldPriority.setEnabled(true);
                        } else {
                            EastPanel_AddProcessPanel.textFieldPriority.setEnabled(false);
                        }
                        Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                        Client.socketSend(new Gson().toJson(Constant.arrayListProcess));
                        ResultAfterExecuteAlgorithm result = new Gson().fromJson(Client.socketReadLine(), ResultAfterExecuteAlgorithm.class);
                        renderGraph(result);
                    } catch (Exception ex) {
                        Logger.getLogger(EastPanel_ChooseTypeAlgorithmPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    renderDefaultGraph();
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(labelType, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(typeAlgorithmArr, gbc);
    }

}
