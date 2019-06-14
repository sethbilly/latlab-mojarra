package com.latlab.mojarra.jsf;


import com.latlab.common.dateutils.DateRange;
import com.latlab.common.dateutils.Period;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "dateRangePicker")
@SessionScoped
public class DateRangePicker implements Serializable 
{

     private DateRange dateRange = new DateRange();
     private Period period;//

     private final List<Period> periodsList = Period.asPastList();

    public DateRangePicker()
    {
        dateRange.setToDate(new Date());
    }
     
     
    public DateRange getDateRange()
    {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange)
    {
        this.dateRange = dateRange;
    }

    public Period getPeriod()
    {
        return period;
    }

    public void setPeriod(Period period)
    {        
        this.period = period;
        
        if(period != null)
        {
            dateRange.updateWithPeriod(period);
        }      
        else
        {
            dateRange.setToDate(null);
            dateRange.setFromDate(null);
        }
    }

    public List<Period> getPeriodsList()
    {
        return periodsList;
    }
    
}
