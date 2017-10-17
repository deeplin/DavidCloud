using Domain.Entities;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Contexts
{
    public class ConsoleContext : DbContext
    {
        // Your context has been configured to use a 'LiftDatabase' connection string from your application's 
        // configuration file (App.config or Web.config). By default, this connection string targets the 
        // 'LiftCloud.Models.LiftDatabase' database on your LocalDb instance. 
        // 
        // If you wish to target a different database and/or database provider, modify the 'LiftDatabase' 
        // connection string in the application configuration file.
        public ConsoleContext() : base("name=ConsoleDatabase")
        {
        }

        // Add a DbSet for each entity type that you want to include in your model. For more information 
        // on configuring and using a Code First model, see http://go.microsoft.com/fwlink/?LinkId=390109.

        public virtual DbSet<DavidConsole> Consoles { get; set; }

        public virtual DbSet<Analog> Analogs { get; set; }

    }
}
