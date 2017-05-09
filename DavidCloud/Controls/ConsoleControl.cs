using DavidCloud.Databases;
using DavidCloud.Models;
using DavidCloud.Packets;
using DavidCloud.Utils;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Controls
{
    class ConsoleControl
    {
        private DavidDatabase mDavidDatabase = new DavidDatabase();

        //Key: console Value: TokenBase64
        private IDictionary<string, string> mConsoleDictionary = new Dictionary<string, string>();
        //Key: TokenBase64 Value Console
        private IDictionary<string, DavidConsole> mTokenDictionary = new Dictionary<string, DavidConsole>();

        public Packet Login(byte[] data, IPEndPoint consoleEndPoint)
        {
            int index = ConsoleRequest.PROTOCOL_HEADER_LENGTH;
            string consoleId = MessageBuilder.GetNext(data, ref index);
            //string user = MessageBuilder.GetNext(data, ref index);
            //string password = MessageBuilder.GetNext(data, ref index);
            string user = "";
            string password = "";
            string modelDevice = MessageBuilder.GetNext(data, ref index);
            string manufacture = MessageBuilder.GetNext(data, ref index);
            string productionDate = MessageBuilder.GetNext(data, ref index);

            DavidConsole console;
            byte[] token;

            lock (this)
            {
                if (mConsoleDictionary.ContainsKey(consoleId))
                {
                    String tokenBase64 = mConsoleDictionary[consoleId];
                    console = mTokenDictionary[tokenBase64];
                    token = Convert.FromBase64String(tokenBase64);
                    console.TokenBase64 = tokenBase64;
                }
                else
                {
                    token = ByteUtil.GetRandom(ConsoleRequest.TOKEN_LENGTH);
                    IUnityContainer consoleContainer = UnityConfig.GetConfiguredContainer();
                    console = consoleContainer.Resolve<DavidConsole>();

                    string tokenBase64 = Convert.ToBase64String(token);
                    mConsoleDictionary[consoleId] = tokenBase64;
                    mTokenDictionary[tokenBase64] = console;

                    console.TokenBase64 = tokenBase64;
                }
            }

            console.InitializeUser(consoleId, user, password, modelDevice, manufacture, productionDate);
            console.InitializeAddress(consoleEndPoint);

            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            DavidConsoleRepository DavidConsoleRepository = container.Resolve<DavidConsoleRepository>();
            DavidConsoleRepository.Save(console);

            return BuildResponse(data, token, consoleEndPoint);
        }

        public Packet HeartBeat(byte[] data, IPEndPoint consoleEndPoint)
        {
            byte[] token = MessageBuilder.GetToken(data);
            string tokenBase64 = Convert.ToBase64String(token);
            lock (this)
            {
                if (mTokenDictionary.ContainsKey(tokenBase64))
                {
                    DavidConsole DavidConsole = mTokenDictionary[tokenBase64];
                    DavidConsole.InitializeAddress(consoleEndPoint);
                }
                else
                {
                    IUnityContainer container = UnityConfig.GetConfiguredContainer();
                    DavidConsoleRepository DavidConsoleRepository = container.Resolve<DavidConsoleRepository>();
                    DavidConsole DavidConsole = DavidConsoleRepository.SelectFromToken(tokenBase64);
                    if (DavidConsole != null)
                    {
                        DavidConsole.InitializeAddress(consoleEndPoint);

                        mConsoleDictionary[DavidConsole.DavidConsoleId] = tokenBase64;
                        mTokenDictionary[tokenBase64] = DavidConsole;
                    }
                }
            }

            return UpdateConsoleResponse(data, null, consoleEndPoint);
        }

        public Packet Location(byte[] data, IPEndPoint consoleEndPoint)
        {
            byte[] token = MessageBuilder.GetToken(data);
            string tokenBase64 = Convert.ToBase64String(token);
            lock (this)
            {
                if (mTokenDictionary.ContainsKey(tokenBase64))
                {
                    IUnityContainer container = UnityConfig.GetConfiguredContainer();
                    BaiduLocation location = container.Resolve<BaiduLocation>();

                    int index = ConsoleRequest.PROTOCOL_HEADER_LENGTH + token.Length;
                    String latitude = MessageBuilder.GetNext(data, ref index);
                    location.Latitude = Double.Parse(latitude);
                    String longitude = MessageBuilder.GetNext(data, ref index);
                    location.Longitude = Double.Parse(longitude);
                    String radius = MessageBuilder.GetNext(data, ref index);
                    location.Radius = float.Parse(radius);
                    location.Address = MessageBuilder.GetNext(data, ref index);
                    location.Describe = MessageBuilder.GetNext(data, ref index);

                    DavidConsole console = mTokenDictionary[tokenBase64];
                    console.BaiduLocation = location;

                    DavidConsoleRepository DavidConsoleRepository = container.Resolve<DavidConsoleRepository>();
                    DavidConsoleRepository.Save(console);
                }
            }
            return UpdateConsoleResponse(data, null, consoleEndPoint);
        }

        private Packet BuildResponse(byte[] data, byte[] token, IPEndPoint consoleEndPoint)
        {
            byte[] buffer;
            if (data.Length < ConsoleRequest.PROTOCOL_HEADER_LENGTH + ConsoleRequest.TOKEN_LENGTH)
            {
                buffer = new byte[ConsoleRequest.PROTOCOL_HEADER_LENGTH + ConsoleRequest.TOKEN_LENGTH];
                Buffer.BlockCopy(data, 0, buffer, 0, ConsoleRequest.PROTOCOL_HEADER_LENGTH);
            }
            else
            {
                buffer = data;
            }

            return UpdateConsoleResponse(buffer, token, consoleEndPoint);
        }

        //发送到手机端
        //public MobileWrapperMessage GetConsoles(byte messageType, IPEndPoint endPoint)
        //{
        //    IUnityContainer container = UnityConfig.GetConfiguredContainer();
        //    MobileSearchResponse mobileSearchCommand = container.Resolve<MobileSearchResponse>();

        //    lock (this)
        //    {
        //        foreach (DavidConsole DavidConsole in mTokenDictionary.Values)
        //        {
        //            mobileSearchCommand.AddConsole(DavidConsole);
        //        }
        //    }

        //    string searchBody = JsonHelper.JsonSerializer<MobileSearchResponse>(mobileSearchCommand);
        //    MobileWrapperMessage mobileWrapperMessage = container.Resolve<MobileWrapperMessage>();
        //    mobileWrapperMessage.MessageType = messageType;
        //    mobileWrapperMessage.MessageBody = searchBody;

        //    return mobileWrapperMessage;
        //}

        public string GetConsoleId(byte[] token)
        {
            string tokenBase64 = Convert.ToBase64String(token);
            foreach (KeyValuePair<string, string> pair in mConsoleDictionary)
            {
                if (pair.Value.Equals(tokenBase64))
                {
                    return pair.Key;
                }
            }
            return null;
        }

        //发送到Console端
        public void SendRequest(string consoleId, byte[] body)
        {
            lock (mConsoleDictionary)
            {
                IUnityContainer container = UnityConfig.GetConfiguredContainer();
                Action<Packet> SendUdpPacket = container.Resolve<Action<Packet>>(Constant.CONSOLE);

                if (mConsoleDictionary.ContainsKey(consoleId))
                {
                    string tokenBase64 = mConsoleDictionary[consoleId];
                    if (mTokenDictionary.ContainsKey(tokenBase64))
                    {
                        DavidConsole console = mTokenDictionary[tokenBase64];

                        Packet packet = container.Resolve<Packet>();
                        packet.Data = body;
                        packet.Length = body.Length;
                        packet.EndPoint = console.ConsoleEndPoint;
                        SendUdpPacket(packet);
                    }
                }
            }
        }

        //将data中的Head拷贝如Response的Head, 将body宝贝入Response的Body
        private Packet UpdateConsoleResponse(byte[] data, byte[] body, IPEndPoint consoleEndPoint)
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            Packet response = container.Resolve<Packet>();
            response.Data = data;
            response.EndPoint = consoleEndPoint;

            data[6]++;
            //Length
            if (body != null)
            {
                data[10] = (byte)(body.Length % 256);
                data[11] = (byte)(body.Length / 256);
                Buffer.BlockCopy(body, 0, data, ConsoleRequest.PROTOCOL_HEADER_LENGTH, body.Length);
                response.Length = ConsoleRequest.PROTOCOL_HEADER_LENGTH + body.Length;
            }
            else
            {
                data[10] = 0;
                data[11] = 0;
                response.Length = ConsoleRequest.PROTOCOL_HEADER_LENGTH;
            }

            //CRC
            data[12] = 0;
            data[13] = 0;
            data[14] = 0;
            data[15] = 0;

            return response;
        }

        private long ComputeTokenId(byte[] token)
        {
            long id = 0;
            foreach (byte b in token)
            {
                id *= 256;
                id += b;
            }
            return id;
        }
    }
}
