namespace DavidCloud.Databases
{
    using DavidCloud.Models;
    using System;
    using System.Data.Entity;
    using System.Linq;

    public class DavidDatabase : DbContext
    {
        // Your context has been configured to use a 'DavidDatabase' connection string from your application's 
        // configuration file (App.config or Web.config). By default, this connection string targets the 
        // 'DavidCloud.Models.DavidDatabase' database on your LocalDb instance. 
        // 
        // If you wish to target a different database and/or database provider, modify the 'DavidDatabase' 
        // connection string in the application configuration file.
        public DavidDatabase()
            : base("name=DavidDatabase")
        {
        }

        // Add a DbSet for each entity type that you want to include in your model. For more information 
        // on configuring and using a Code First model, see http://go.microsoft.com/fwlink/?LinkId=390109.

        public virtual DbSet<DavidConsole> DavidConsoles { get; set; }
    }
}