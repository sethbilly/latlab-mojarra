/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latlab.mojarra.jsf;

import com.stately.common.utils.ProcessingMessage;
import java.util.List;
import org.omnifaces.util.Messages;

public class Msg {

    public static final String DISALLOW_TRANSACTION = "Transaction is not allowed because transaction date is before the current EoD Date. "
            + " Please reverse/delete EoD up to the date you want to execute the changes for";

    public static void successSave() {
        JsfUtil.addInformationMessage("Saved successfully");
    }

    public static void msg(boolean result) {
        if (result) {
            info("Action Successful");
        } else {
            error("Action Failed");
        }
    }

    public static void failedSave() {
        Messages.addGlobalError("Saving failed");
    }

    public static void successDelete() {
        Messages.addGlobalInfo("Deleted successfully");
    }

    public static void failedDelete() {
        Messages.addGlobalError("Deleting failed");
    }

    public static void reportInitiated() {
        Messages.addGlobalInfo("Report generation initiated");
    }

    public static void genericInfo(String info) {
        info(info);
    }

    public static void info(String info) {
        JsfUtil.addInformationMessage(info);
    }

    public static void error(String info) {
        JsfUtil.addErrorMessage(info);
    }

    public static void genericError(String error) {
        JsfUtil.addErrorMessage(error);
    }

    public static void print(ProcessingMessage processingMessage) {

        List<ProcessingMessage> messagesList = processingMessage.getList();

        for (ProcessingMessage object : messagesList) {
            if (object.getType() == ProcessingMessage.Severity.FATAL) {
                error(object.getMsg());
            } else if (object.getType() == ProcessingMessage.Severity.INFO) {
                info(object.getMsg());
            } else if (object.getType() == ProcessingMessage.Severity.WARN) {
                error(object.getMsg());
            }
        }
    }
}
