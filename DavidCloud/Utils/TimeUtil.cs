using System;

namespace DavidCloud.Utils
{
    class TimeUtil
    {
        public static DateTime ConvertJavaDateTimeToNetTime(long time_JAVA_Long)//java长整型日期，毫秒为单位 
        {
            DateTime dt_1970 = new DateTime(1970, 1, 1, 0, 0, 0);
            long tricks_1970 = dt_1970.Ticks;//1970年1月1日刻度                         
            long time_tricks = tricks_1970 + time_JAVA_Long * 10000;//日志日期刻度                        
            DateTime dateTime = new DateTime(time_tricks).AddHours(8);//转化为DateTime
            return dateTime;
        }
    }
}
