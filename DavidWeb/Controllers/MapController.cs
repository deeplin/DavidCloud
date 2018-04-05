using Domain.Contexts;
using Domain.Entities;
using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;

namespace DavidWeb.Controllers
{
    [Authorize]
    public class MapController : Controller
    {
        private ConsoleRepository consoleRepository;
        public MapController(ConsoleRepository consoleRepository)
        {
            this.consoleRepository = consoleRepository;
        }

        public ActionResult Index()
        {
            ConsoleRepository consoleRepository = new ConsoleRepository();
            IEnumerable<DavidConsole> davidConsoles =
                consoleRepository.GetConsoles().AsEnumerable();
            return View(davidConsoles);
        }
    }
}