//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//
//  The Initial Developer of the Original Code is VMware, Inc.
//  Copyright (c) 2007-2011 VMware, Inc.  All rights reserved.
//


package com.rabbitmq.client.test.server;

import com.rabbitmq.client.test.functional.RabbitBrokerTestCase;

import java.io.IOException;

public class PersisterRestart2 extends RabbitBrokerTestCase {

    private static final String Q1 = "Restart2One";
    private static final String Q2 = "Restart2Two";

    protected void exercisePersister(String q) 
      throws IOException
    {
        basicPublishPersistent(q);
        basicPublishVolatile(q);
    }

    public void testRestart()
        throws IOException, InterruptedException
    {
        declareDurableQueue(Q1);
        declareDurableQueue(Q2);
        exercisePersister(Q1);
        exercisePersister(Q2);
        // Those will be in the incremental snapshot then
        exercisePersister(Q1);
        exercisePersister(Q2);
        
        restart();
        
        assertDelivered(Q1, 2);
        assertDelivered(Q2, 2);
        deleteQueue(Q2);
        deleteQueue(Q1);
    }

}
