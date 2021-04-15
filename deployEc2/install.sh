for (( i = 1; i < 5; i++ )); do
    echo "Enter Ip Address for $i"
    read -r ip
    ssh -i "../../aakash6650.pem" ec2-user@"$ip"
    echo "$ip:8080/manager/html/list"
done
