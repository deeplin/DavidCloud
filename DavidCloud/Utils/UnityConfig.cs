using Microsoft.Practices.Unity;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Utils
{
    static class UnityConfig
    {
        #region Unity Container
        private static Lazy<IUnityContainer> container = new Lazy<IUnityContainer>(() =>
        {
            var container = new UnityContainer();
            RegisterTypes(container);
            return container;
        });

        /// <summary>
        /// Gets the configured Unity container.
        /// </summary>
        public static IUnityContainer GetConfiguredContainer()
        {
            return container.Value;
        }
        #endregion

        /// <summary>Registers the type mappings with the Unity container.</summary>
        /// <param name="container">The unity container to configure.</param>
        /// <remarks>There is no need to register concrete types such as controllers or API controllers (unless you want to 
        /// change the defaults), as Unity allows resolving a concrete type even if it was not previously registered.</remarks>
        public static void RegisterTypes(IUnityContainer container)
        {
            // TODO: Register your types here
            //Networks
            //container.RegisterType<IConsoleServer, ConsoleServer>(new ContainerControlledLifetimeManager());
            //container.RegisterType<IMobileServer, MobileServer>(new ContainerControlledLifetimeManager());

            ////Databases
            //container.RegisterType<DavidConsoleRepository>(new ContainerControlledLifetimeManager());

            ////Controls
            //container.RegisterType<ConsoleControl>(new ContainerControlledLifetimeManager());
            //container.RegisterType<MobileControl>(new ContainerControlledLifetimeManager());
            //container.RegisterType<RequstHandler>(new ContainerControlledLifetimeManager());
            //container.RegisterType<ConsoleObserver>(new ContainerControlledLifetimeManager());
            //container.RegisterType<MobileObserver>(new ContainerControlledLifetimeManager());

            ////Messages
            //container.RegisterType<ConsoleRequest>();
            //container.RegisterType<MobileWrapperMessage>();
            //container.RegisterType<Packet>();

            ////Models
            //container.RegisterType<DavidConsole>();
            //container.RegisterType<Mobile>();
            //container.RegisterType<BaiduLocation>();

            ////commands
            //container.RegisterType<ConsoleCommand>();
            //container.RegisterType<MobileSearchResponse>();


        }
    }
}
