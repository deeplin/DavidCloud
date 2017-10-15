using DavidCloud.Enums;
using DavidCloud.Models;
using DavidCloud.Utils;
using DotNetty.Handlers.Logging;
using DotNetty.Transport.Channels;
using NLog;
using System;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Handlers
{
    class ConsoleLogHandler : LoggingHandler
    {

        public const String IN = "L>";
        public const String DOUBLE_IN = "L>>";
        public const String OUT = "L<";
        public const String DOUBLE_OUT = "L<<";

        private static readonly Logger nLogger = LogManager.GetCurrentClassLogger();

        public ConsoleLogHandler(string name) : base(name)
        {
        }

        public override void ChannelRead(IChannelHandlerContext ctx, object message)
        {
            if (this.Logger.IsEnabled(this.InternalLevel))
            {
                this.Logger.Log(this.InternalLevel, this.Format(ctx, "RECEIVED", message));
                nLogger.Info(Format(ctx, "RECEIVED", message));
            }
            ctx.FireChannelRead(message);
        }

        public override Task WriteAsync(IChannelHandlerContext ctx, object message)
        {
            if (this.Logger.IsEnabled(this.InternalLevel))
            {
                this.Logger.Log(this.InternalLevel, this.Format(ctx, "WRITE", message));
                nLogger.Info(Format(ctx, "WRITE", message));
            }
            return ctx.WriteAsync(message);
        }

        private static int sequence = 0;

        public static void LogPacket(string direction, ConsolePacket consolePacket)
        {
            MessageTypeEnum messageType = (MessageTypeEnum)consolePacket.MessageType;
            string operationString;
            if (messageType == MessageTypeEnum.LadderOriented)
            {
                operationString = consolePacket.Operation.ToString();
            }
            else if (messageType == MessageTypeEnum.CloudOriented)
            {
                operationString = ((OperationEnum)consolePacket.Operation).ToString();
            }
            else
            {
                operationString = consolePacket.Operation.ToString();
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.Append(String.Format("{0}: {1} {2}", sequence, direction, messageType));
            stringBuilder.Append(String.Format(" OP={0}", operationString));
            stringBuilder.Append(String.Format(" SQ={0}", consolePacket.Sequence));
            stringBuilder.Append(String.Format(" TD={0}", consolePacket.Thread));
            stringBuilder.Append(String.Format(" LT={0:D3}", consolePacket.Length));
            stringBuilder.Append(String.Format(" EP={0}", PacketUtil.IPEndPointToString(consolePacket.RemoteEndPoint)));
            stringBuilder.Append(" BD=" + StringUtil.ByteToHexString(consolePacket.Body));
            nLogger.Info(stringBuilder.ToString());
            nLogger.Warn(stringBuilder.ToString());
            sequence++;
        }

        public static void Log(string direction, string info)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.Append(String.Format("{0}: {1} {2}", sequence, direction, info));

            nLogger.Info(stringBuilder.ToString());
            nLogger.Warn(stringBuilder.ToString());
            sequence++;
        }

        public static void LogAll(Exception exception)
        {
            nLogger.Info(exception);
            nLogger.Warn(exception);
            nLogger.Error(exception);
        }
    }
}