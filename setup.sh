apt install openjdk-8-jre-headless

if [ ! -d ~/bin/ ]
then
  mkdir ~/bin/
fi
wget -O ~/bin/lsOktalACL.class https://github.com/user9209/show-octal-acls-in-ls/releases/download/0.1/lsOktalACL.class

echo >> ~/.bashrc "alias ls='ls -A -l -h --color=auto | java -cp ~/bin/ lsOktalACL'"