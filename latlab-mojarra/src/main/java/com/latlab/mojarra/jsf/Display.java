/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.latlab.mojarra.jsf;

import com.stately.common.utils.ProcessingMessage;
import java.util.List;
import org.omnifaces.util.Messages;

public class Display
{
    
    public static void print(ProcessingMessage processingMessage)
    {
        if (processingMessage.getState() != null)
        {
            if (processingMessage.isSuccessfull())
            {
                Messages.addGlobalInfo("Action was successful");
            } else
            {
                Messages.addGlobalFatal("Action was unsuccessful");
            }
//            Messages.addGlobalFatal("Requested asction was not successfully completed");
        }
        
        
        List<ProcessingMessage> messagesList = processingMessage.getList();
        
        for (ProcessingMessage object : messagesList)
        {
            if(object.getType() == ProcessingMessage.Severity.FATAL)
            {
                Messages.addGlobalError(object.getMsg());
            }
            else if(object.getType() == ProcessingMessage.Severity.INFO)
            {
                Messages.addGlobalInfo(object.getMsg());
            }
            else if(object.getType() == ProcessingMessage.Severity.WARN)
            {
                Messages.addGlobalWarn(object.getMsg());
            }
        }
    }
}
