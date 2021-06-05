echo "How many servers do you wish to deploy"
read -r servers
for (( i = 1; i < "$servers"; i++ )); do
    echo "Enter Ip Address for $i"
    read -r ip
    ssh -i "../../aakash6650.pem" ec2-user@"$ip"
    echo "$ip:8080/manager/html/list"
    open --new -a "Google Chrome" --args "$ip:8080/manager/html/list"
done
