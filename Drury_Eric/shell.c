#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define BUFFER_LEN 1024
extern char **environ;

int main(int argc, char *argv[])
{
   if(argc>1){
	int newstdin=open(argv[1],"r");	//check for input, and set it to stdin if so
	close(0);
	dup(newstdin);
	close(newstdin);
    }
    char line[BUFFER_LEN];  
    char* args[100];        
    char* path= "/bin/";
    char dir[1024];    
    char progpath[20];      
    int count;
    int run;  
    int a=0;             

while(1){
    run=0;
    getcwd(dir,sizeof(dir));			//get current directory, then print it
    printf("myShell:%s>",dir);                    

        if(!fgets(line, BUFFER_LEN, stdin)){  
        break;                                
    }
    size_t length=strlen(line);
    if(line[length-1]=='\n')		//formatting the strings, and checking for quit/&
        line[length-1]='\0';
    if(line[length-1]=='&'||line[length-2]=='&')
	run=1;
    if(strcmp(line, "quit")==0){            
        break;
    }

    char *token;                 
    token = strtok(line," ");
    int i=0;

    while((token!=NULL )&& (i<100)){  //parse the string into arguments
        args[i]=token;      
        token = strtok(NULL," ");
    	i++;
    }

    args[i]=NULL;                     

    count=i;                          
    for(i=0; i<count; i++){			//checks for and handles I/O redirection
        if(args[i]=="<"){
	    int newstdin=open(args[i+1],"r");
	    close(0);
	    dup(newstdin);
	    close(newstdin);
	    args[i]=NULL;
	    i++;
	    for(a=0;i<count;i++){
		  args[a]=args[i];
		    a++;
	    } 
	     
	}
	if(args[i]==">"){
	    int newstdout=open(args[i+1],"r");
	    close(0);
	    dup(newstdout);
	    close(newstdout);
	    args[i]=NULL;  
	    i++;
	    for(a=0;i<count;i++){
		  args[a]=args[i];
		    a++;
	    } 
	}
	if(args[i]=="|"){		//checks for piping, then runs the first command, while preparing the second
	    args[i]='\0';  
	    strcpy(progpath, path);           
            strcat(progpath, args[0]);            

    	    for(i=0; i<strlen(progpath); i++){    
                if(progpath[i]=='\n'){      
           	    progpath[i]='\0';
        	}
    	    }frk(progpath,args,run);
	    
	    i++;
	    for(a=0;i<count;i++){
		  args[a]=args[i];
	  	  a++;
  	}    
    }
    strcpy(progpath, path);           
    strcat(progpath, args[0]);            

    for(i=0; i<strlen(progpath); i++){    
        if(progpath[i]=='\n'){      
            progpath[i]='\0';
        }
    }
	

    
	frk(progpath,args,run);
} 
}
}

int frk(char progpath[],char* args[],int run){

    if(strcmp(args[0],"cd")==0)			//checks for commands then executes them
        chdir(args[1]);
    else if(strcmp(args[0],"clr")==0)
	system("clear");
    else if(strcmp(args[0],"environ")==0){
	char **env;
	for (env = environ; *env; ++env)
        printf("%s\n", *env);}
    else if(strcmp(args[0],"pause")==0){
	
	}
    else if(strcmp(args[0],"help")==0)
	printf("cd:chang dir \n clr:clear screen \n dir <directory>: lists contents of directory \n environ: list all environment strings \n echo <command>:echo's command back to shell \n help: displays help \n pause:pauses operation \n quit: quits shell \n");
    else{
    int pid= fork();              //fork call

    if(pid==0){               
        execvp(progpath,args);		//program executed
        fprintf(stderr, "Error\n");

    }else{
	if(run==0)                    
        wait(NULL);		//either return immediatly if & present, or wait for child
       printf("\n");
    }

}
}
