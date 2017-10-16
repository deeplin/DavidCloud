using DavidCloud.Handlers;
using DotNetty.Handlers.Logging;
using DotNetty.Transport.Bootstrapping;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using NLog;
using System;
using System.Threading.Tasks;

namespace DavidCloud.Controllers
{
    public class ConsoleControl
    {
        private static readonly Logger logger = LogManager.GetCurrentClassLogger();

        MultithreadEventLoopGroup consoleGroup;
        IChannel consoleBoundChannel;

        public ConsoleControl()
        {
            consoleGroup = new MultithreadEventLoopGroup();
            consoleBoundChannel = null;
        }

        public async Task Start(int port)
        {
            var consoleBootstrap = new Bootstrap();
            consoleBootstrap
                .Group(consoleGroup)
                .Channel<SocketDatagramChannel>()
                .Handler(new LoggingHandler("CONSOLE-SRV"))
                .Handler(new ActionChannelInitializer<IChannel>(channel =>
                {
                    IChannelPipeline pipeline = channel.Pipeline;

                    pipeline.AddLast(new ConsoleLogHandler("CONSOLE-CONN"));
                    //pipeline.AddLast(new DatagramDecoder());
                    //pipeline.AddLast(new DatagramEncoder());
                    //pipeline.AddLast(new DatagramHandler());
                }));

            consoleBoundChannel = await consoleBootstrap.BindAsync(port);
        }

        public async Task Stop()
        {
            if (consoleBoundChannel != null)
            {
                await consoleBoundChannel.CloseAsync();
            }
            await consoleGroup.ShutdownGracefullyAsync(TimeSpan.FromMilliseconds(100), TimeSpan.FromSeconds(1));
        }
    }
}
