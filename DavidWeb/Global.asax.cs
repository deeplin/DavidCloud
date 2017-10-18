using Domain.Contexts;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;

namespace DavidWeb
{
    public class MvcApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            using (ConsoleContext consoleContext = new ConsoleContext())
            {
                consoleContext.Database.CreateIfNotExists();
            }
            
            AreaRegistration.RegisterAllAreas();
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
        }
    }
}
