using Microsoft.Practices.Unity;
using System;

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

            //#region Ladders
            ////Server Strategys
            //container.RegisterType<LoginStrategy>(new ContainerControlledLifetimeManager());
            //container.RegisterType<HeartBeatStrategy>(new ContainerControlledLifetimeManager());
            ////Client Strategys
            //container.RegisterType<HeartBeatPlusRequestStrategy>(new ContainerControlledLifetimeManager());
            //container.RegisterType<HeartBeatPlusResponseStrategy>(new ContainerControlledLifetimeManager());
            //container.RegisterType<RpcRequestStrategy>(new ContainerControlledLifetimeManager());
            //container.RegisterType<RpcResponseStrategy>(new ContainerControlledLifetimeManager());
            //container.RegisterType<WaveRequestStrategy>(new ContainerControlledLifetimeManager());
            //container.RegisterType<WaveResponseStrategy>(new ContainerControlledLifetimeManager());
            ////Controllers
            //container.RegisterType<LadderControl>(new ContainerControlledLifetimeManager());
            ////Models
            //container.RegisterType<LadderPacket>();
            //#endregion
        }
    }
}
