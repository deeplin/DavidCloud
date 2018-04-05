namespace DavidCloud.Utils
{
    public static class ServerSetting
    {
        public static bool IsSsl
        {
            get
            {
                string ssl = SystemHelper.Configuration["ssl"];
                return !string.IsNullOrEmpty(ssl) && bool.Parse(ssl);
            }
        }

        public static int LadderPort => int.Parse(SystemHelper.Configuration["consolePort"]);

        public static int RestPort => int.Parse(SystemHelper.Configuration["restPort"]);
    }
}
